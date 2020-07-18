package com.lyf.scm.core.remote.stock.facade;

import com.lyf.scm.core.remote.stock.dto.ChannelSales;
import com.lyf.scm.core.remote.stock.dto.ReservationDTO;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test-zt")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockFacadeTest {

    @Resource
    private StockFacade stockFacade;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Test
    public void createReservation() {
        ReservationDTO reservationInDTO = new ReservationDTO();
        reservationInDTO.setAddress("上海九亭");
        reservationInDTO.setBusinessType(2);
        reservationInDTO.setChannelCode("G20000000_119");
        reservationInDTO.setCity("上海市");
        reservationInDTO.setCityCode("310100");
        reservationInDTO.setCounty("徐汇区");
        reservationInDTO.setCountyCode("310104");
        reservationInDTO.setMobile("18062564683");
        reservationInDTO.setName("大金宝");
        reservationInDTO.setOutRecordCode("570818592392019968");
        reservationInDTO.setProvince("上海");
        reservationInDTO.setProvinceCode("310000");
        reservationInDTO.setOutCreateTime("2020-04-26 15:15:12");

        List<ReservationDTO.ReservationDetailDTO> reservationDetailInDTOS = new ArrayList<>();
        ReservationDTO.ReservationDetailDTO reservationDetailInDTO = new ReservationDTO.ReservationDetailDTO();
        reservationDetailInDTO.setSkuCode("11442");
        reservationDetailInDTO.setSkuQty(new BigDecimal("5.00"));
        reservationDetailInDTO.setUnitCode("PAK");
        reservationDetailInDTOS.add(reservationDetailInDTO);
        reservationInDTO.setReservationDetails(reservationDetailInDTOS);

        ReservationDTO reservationDTO = stockFacade.createReservation(reservationInDTO);
        System.out.println(reservationDTO);
    }

    @Test
    public void lockReservationByRecordCode() {
        ReservationDTO reservationDTO = stockFacade.lockReservationByRecordCode("18062564683");
        System.out.println(reservationDTO);
    }

    @Test
    public void queryVirtualWarehouseByCodes() {
        List<String> virtualWarehouseCodes = new ArrayList<>();
        virtualWarehouseCodes.add("C001-1");
        List<VirtualWarehouse> list = stockRealWarehouseFacade.queryVirtualWarehouseByCodes(virtualWarehouseCodes);
        System.out.println(list);
    }

    @Test
    public void cancelOrder() {
    }

    @Test
    public void cancelDo() {
    }

    @Test
    public void createReturnRecord() {
    }

    @Test
    public void createReservationDo() {
    }

    @Test
    public void queryByOutCodeAndFactoryCodeList() {
        ChannelSales channelSales = stockRealWarehouseFacade.queryByChannelCode("G20000000_119");
        System.out.println(channelSales);
    }
}