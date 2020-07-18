package com.lyf.scm.core.remote.base;


import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.dto.SaleOrgDTO;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "basedata-core")
public interface BaseRemoteService {

    /**
     * 根据门店CODE获取门店信息
     * @param code
     * @return
     */
    @RequestMapping(value = "/v1/store/searchByCode",method = RequestMethod.GET)
    Response<StoreDTO> searchByCode(@RequestParam("code") String code);

    /**
     * 根据门店CODE批量获取门店信息
     * @param shopCodeList
     * @return
     */
    @RequestMapping(value = "/v1/store/searchByCodeList",method = RequestMethod.POST)
    Response<List<StoreDTO>> searchByCodeList(@RequestBody List<String> shopCodeList);

    /**
     * 根据销售组织code查询销售组织的详细信息
     * @param orgCode
     * @return
     */
    @RequestMapping(value = "/saleorg/v1/search/saleorg/{orgCode}",method = RequestMethod.GET)
    Response<SaleOrgDTO> getOrgByOrgCode(@PathVariable("orgCode") String orgCode);

    /**
     * 根据渠道编码批量查询渠道信息
     * @param channelCodeList
     * @return
     */
    @RequestMapping(value = "/v1/channelList/channelCodeList",method = RequestMethod.POST)
    Response<List<ChannelDTO>> queryChannelInfoByChannelCodeList(@RequestBody List<String> channelCodeList);

    /**
     * 根据类型查询门店列表
     * @param storeDTO A门店B工厂
     * @return
     */
    @RequestMapping(value = "/v1/store/list",method = RequestMethod.POST)
    Response<List<StoreDTO>> searchByType(@RequestBody StoreDTO storeDTO);

    /**
     * 根据code获取门店名称
     * @param code
     * @return
     */
    @RequestMapping(value = "/v1/store/searchByCode",method = RequestMethod.GET)
    Response<StoreDTO> searchShopName(@RequestParam("code") String code);

    /**
     * 根据工厂名称模糊查询工厂
     * @param pageNum
     * @param pageSize
     * @param storeDTO
     * @return
     */
    @RequestMapping(value = "/v1/store/searchByCode",method = RequestMethod.POST)
    Response<PageInfo<StoreDTO>> searchStoreByName(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize")  Integer pageSize,@RequestBody StoreDTO storeDTO);

    /**
     * @Description: 根据公司Code批量查询公司门店 <br>
     * @Author chuwenchao 2020/2/6
     * @param companyCodes
     * @return
     */
    @RequestMapping(value = "/v1/store/selectStoreByCompanyCodeList",method = RequestMethod.POST)
    Response<PageInfo<StoreDTO>> selectStoreByCompanyCodeList(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize")  Integer pageSize,@RequestBody List<String> companyCodes);

    /**
     * @Description: 根据加盟商Code批量查询加盟商门店信息 <br>
     * @Author chuwenchao 2020/2/6
     * @param merchantCodes
     * @return
     */
    @RequestMapping(value = "/v1/store/selectStoreByFranchiseeList",method = RequestMethod.POST)
    Response<PageInfo<StoreDTO>> selectStoreByFranchiseeList(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize")  Integer pageSize,@RequestBody List<String> merchantCodes);
}
