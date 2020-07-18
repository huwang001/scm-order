package com.lyf.scm.core.remote.item.facade;

import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemFacadeTest {
    @Resource
    private ItemFacade itemFacade;

    @Test
    public void skuListBySkuCodes() {
        List<String> skuCodeList = new ArrayList<>();
        skuCodeList.add("12234");
        List<SkuInfoExtDTO> list = itemFacade.skuListBySkuCodes(skuCodeList);
        System.out.println(list);
    }

    @Test
    public void unitsBySkuCodeAndMerchantId() {
        List<ParamExtDTO> paramExtDTO = new ArrayList<>();
        ParamExtDTO paramExt = new ParamExtDTO();
        paramExt.setMerchantId(10000L);
        paramExt.setSkuCode("12234");
        paramExtDTO.add(paramExt);
        List<SkuUnitExtDTO> list = itemFacade.unitsBySkuCodeAndMerchantId(paramExtDTO);
        System.out.println(list);
    }

}