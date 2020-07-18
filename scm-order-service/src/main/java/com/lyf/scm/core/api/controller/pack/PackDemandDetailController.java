package com.lyf.scm.core.api.controller.pack;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.pack.PackDemandDetailDTO;
import com.lyf.scm.core.api.dto.pack.SkuAndCombineDTO;
import com.lyf.scm.core.api.dto.pack.SkuParamDTO;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuAttributeInfo;
import com.lyf.scm.core.remote.item.dto.SkuTypeDTO;
import com.lyf.scm.core.service.pack.PackDemandDetailService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zys
 * @Remarks
 * @date 2020/7/6
 */
@Slf4j
@RestController
@Api(tags = "包装需求单成品商品")
@RequestMapping("/order/v1/pack/demandDetail")
public class PackDemandDetailController {

    @Resource
    private PackDemandDetailService packDemandDetailService;

    @ApiOperation(value="批量新增或修改成品商品",nickname = "addFinishProductSkuDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200,message = "success")
    @RequestMapping(value="/addAssemblySkuDetail",method = RequestMethod.POST)
    public Response addFinishProductSkuDetail(@RequestBody List<PackDemandDetailDTO> detailDTOS, Long userId ){
        try {
            log.info("批量新增或修改需求单成品商品明细 >>> {}",JSON.toJSONString(detailDTOS),JSON.toJSONString(userId));
            packDemandDetailService.batchSavePackDemandComponent(detailDTOS, userId);
            return Response.builderSuccess("");
        }catch(RomeException e){
            log.info(e.getMessage(),e);
            return Response.builderFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.info(e.getMessage(),e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value="根据需求单号查询成品商品信息",nickname = "queryupdateFinishProductSkuDetail",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200,message = "success")
    @RequestMapping(value = "/queryFinishProductSkuDetail" ,method = RequestMethod.POST)
    public Response<List<PackDemandDetailDTO>> queryFinishProductSkuDetail(@ApiParam(name = "需求单号",value = "requireCode") @RequestParam String requireCode){
        try{
            log.info("查询成品商品信息 入参 >>>{}",JSON.toJSONString(requireCode));
            List<PackDemandDetailDTO> list= packDemandDetailService.queryFinishProductSkuDetail(requireCode);
            return Response.builderSuccess(list);
        }catch(RomeException e){
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value="查询商品类型",nickname = "skuType",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200,message = "success")
    @RequestMapping(value = "/skuType" ,method = RequestMethod.GET)
    public  Response<List<SkuTypeDTO>> skuType(){
        try{
            List<SkuTypeDTO> skuTypeDTOS = packDemandDetailService.skuType();
            return Response.builderSuccess(skuTypeDTOS);
        }catch(RomeException e){
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    /**
     *查询商品信息
     * @param skuParamDTO
     * @return
     */
    @ApiOperation(value="查询商品信息",nickname = "pageSkuInfo",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200,message = "success")
    @RequestMapping(value = "/pageSkuInfo" ,method = RequestMethod.POST)
    public Response<PageInfo<SkuAttributeInfo>> pageSkuInfo(@RequestBody SkuParamDTO skuParamDTO){
        try{
            log.info("包装需求单-商品展示 入参 >>>{}",JSON.toJSONString(skuParamDTO));
            PageInfo<SkuAttributeInfo> skuList =  packDemandDetailService.pageSkuInfo(skuParamDTO);
            return Response.builderSuccess(skuList);
        }catch(RomeException e){
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value="查询商品信息及组合品子品信息",nickname = "queryCombinesBySkuCodeAndUnitCode",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200,message = "success")
    @RequestMapping(value = "/queryCombinesBySkuCodeAndUnitCode" ,method = RequestMethod.POST)
    public Response<SkuAndCombineDTO> queryCombinesBySkuCodeAndUnitCode(@RequestBody List<ParamExtDTO> speList,@RequestParam("packType") Integer packType){
        try{
            log.info("获取组合品及子品信息 入参 >>>{}",JSON.toJSONString(speList));
            SkuAndCombineDTO skuAndCombineInfo = packDemandDetailService.queryCombinesBySkuCodeAndUnitCode(speList,packType);
            return Response.builderSuccess(skuAndCombineInfo);
        }catch(RomeException e){
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
