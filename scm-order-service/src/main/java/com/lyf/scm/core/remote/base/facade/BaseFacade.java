package com.lyf.scm.core.remote.base.facade;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.base.BaseRemoteService;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.dto.SaleOrgDTO;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.rome.arch.core.clientobject.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * com.lyf.scm.core.remote.trade.facade
 *
 * @author zhangxu
 * @date 2020/4/13
 */
@Slf4j
@Component
@AllArgsConstructor
public class BaseFacade {

    private final int PAGE_SIZE = 100;

    @Resource
    private BaseRemoteService baseRemoteService;

    /**
     * 根据门店CODE获取门店信息
     *
     * @param shopCode
     * @return
     */
    public StoreDTO searchByCode(String shopCode) {
        if (StringUtils.isBlank(shopCode)) {
            return new StoreDTO();
        }
        Response<StoreDTO> response = baseRemoteService.searchByCode(shopCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据门店CODE批量获取门店信息
     */
    public List<StoreDTO> searchByCodeList(List<String> shopCodeList) {
        if (CollectionUtils.isEmpty(shopCodeList)) {
            return new ArrayList<StoreDTO>();
        }
        //TODO
        List<StoreDTO> stores = new ArrayList<StoreDTO>();
        // 集合分割处理
        List<List<String>> partList = ListUtils.partition(shopCodeList, PAGE_SIZE);
        for (int i = 0; i < partList.size(); i++) {
            List<String> subList = partList.get(i);
            Response<List<StoreDTO>> response = baseRemoteService.searchByCodeList(subList);
            ResponseValidateUtils.validResponse(response);
            stores.addAll(response.getData());
        }
        return stores;
    }

    /**
     * 根据销售组织code查询销售组织的详细信息
     */
    public SaleOrgDTO getOrgByOrgCode(String orgCode) {
        if (StringUtils.isBlank(orgCode)) {
            return new SaleOrgDTO();
        }
        Response<SaleOrgDTO> response = baseRemoteService.getOrgByOrgCode(orgCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据渠道编码批量查询渠道信息
     */
    public List<ChannelDTO> queryChannelInfoByChannelCodeList(List<String> channelCodeList) {
        //TODO
        if (CollectionUtils.isEmpty(channelCodeList)) {
            return new ArrayList<ChannelDTO>();
        }
        List<ChannelDTO> channelList = new ArrayList<ChannelDTO>();
        // 集合分割处理
        List<List<String>> partList = ListUtils.partition(channelCodeList, PAGE_SIZE);
        Response<List<ChannelDTO>> response;
        for (int i = 0; i < partList.size(); i++) {
            List<String> subList = partList.get(i);
                response = baseRemoteService.queryChannelInfoByChannelCodeList(subList);
                ResponseValidateUtils.validResponse(response);
                channelList.addAll(response.getData());
        }
        return channelList;
    }

    /**
     * 查询单个渠道信息
     * @param channelCode
     * @return
     */
    public ChannelDTO queryChannelInfoByCode(String channelCode) {
        List<ChannelDTO> channelList = queryChannelInfoByChannelCodeList(Lists.newArrayList(channelCode));
        if (null != channelList && channelList.size() > 0) {
            return channelList.get(0);
        }
        return null;
    }

    /**
     * 根据类型查询门店列表
     *
     * @param shopType A门店B工厂
     * @return
     */
    public List<StoreDTO> searchByType(String shopType) {
        if (StringUtils.isBlank(shopType)) {
            return new ArrayList<>();
        }
        StoreDTO param = new StoreDTO();
        param.setType(shopType);
        Response<List<StoreDTO>> response = baseRemoteService.searchByType(param);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据code获取门店名称
     *
     * @param shopCode
     * @return
     */
    public String searchShopName(String shopCode) {
        if (StringUtils.isBlank(shopCode)) {
            return null;
        }
        Response<StoreDTO> response = baseRemoteService.searchShopName(shopCode);
        ResponseValidateUtils.validResponse(response);
        StoreDTO storeDTO = response.getData();
        return storeDTO.getName();
    }

    /**
     * 根据工厂名称模糊查询工厂
     *
     * @param pageNum
     * @param pageSize
     * @param storeDTO
     * @return
     */
    public PageInfo<StoreDTO> searchStoreByName(Integer pageNum, Integer pageSize, StoreDTO storeDTO) {
        if(storeDTO == null) {
            return new PageInfo<>();
        }
        Response<PageInfo<StoreDTO>> response = baseRemoteService.searchStoreByName(pageNum, pageSize, storeDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * @Description: 根据公司Code批量查询公司门店 <br>
     *
     * @Author chuwenchao 2020/2/6
     * @param companyCodes
     * @return
     */
    public List<StoreDTO> selectStoreByCompanyCodeList(Integer pageNum, Integer pageSize, List<String> companyCodes) {
        if(CollectionUtils.isEmpty(companyCodes)) {
            return new ArrayList<>();
        }
        Response<PageInfo<StoreDTO>> response = baseRemoteService.selectStoreByCompanyCodeList(pageNum,pageSize,companyCodes);
        ResponseValidateUtils.validResponse(response);
        PageInfo<StoreDTO> pageInfo = response.getData();
        List<StoreDTO> storeList = pageInfo.getList();
        boolean flag = pageInfo.isIsLastPage();
        while(!flag) {
            pageNum ++;
            response = baseRemoteService.selectStoreByCompanyCodeList(pageNum, pageSize, companyCodes);
            ResponseValidateUtils.validResponse(response);
            PageInfo<StoreDTO> nextPageInfo = response.getData();
            flag = nextPageInfo.isIsLastPage();
            storeList.addAll(nextPageInfo.getList());
        }
        return storeList;
    }

    /**
     * @Description: 根据加盟商Code批量查询加盟商门店信息 <br>
     *
     * @Author chuwenchao 2020/2/6
     * @param merchantCodes
     * @return
     */
    public List<StoreDTO> selectStoreByFranchiseeList(Integer pageNum, Integer pageSize, List<String> merchantCodes) {
        if(CollectionUtils.isEmpty(merchantCodes)) {
            return new ArrayList<>();
        }
        Response<PageInfo<StoreDTO>> response = baseRemoteService.selectStoreByFranchiseeList(pageNum,pageSize,merchantCodes);
        ResponseValidateUtils.validResponse(response);
        PageInfo<StoreDTO> pageInfo = response.getData();
        List<StoreDTO> storeList = pageInfo.getList();
        boolean flag = pageInfo.isIsLastPage();
        while(!flag) {
            pageNum ++;
            response = baseRemoteService.selectStoreByFranchiseeList(pageNum,pageSize,merchantCodes);
            ResponseValidateUtils.validResponse(response);
            PageInfo<StoreDTO> nextPageInfo = response.getData();
            flag = nextPageInfo.isIsLastPage();
            storeList.addAll(nextPageInfo.getList());
        }
        return storeList;
    }

}