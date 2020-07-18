
 require.config({
        paths: {
            "order-timeline-item": 'order-timeline-item.umd.min',
            "order-timeline": 'order-timeline.umd.min',
        }
    });
lyfdefine(['scm-order-import-lib','order-timeline-item','order-timeline','style/scm-order-style','scm-order-detail-components/scm-order-detail-store',
    'scm-order-detail-components/scm-order-detail-ordinary',
    'scm-order-detail-components/scm-order-detail-advance'
], function (lib,style,l,c, StoreDetail,OrdinaryDetail,AdvanceDetail) {
    return ({
        name: 'scm-order-detail',
        components: {
            StoreDetail,
            OrdinaryDetail,
            AdvanceDetail
        },
        data () {
            return {
                orderTypeCode: '2'
            }
        },
        watch: {
            $route: {
                handler (n) {
                    if(n.name==="scm-order-detail"){
                        this.orderTypeCode = n.query.orderTypeCode ? n.query.orderTypeCode.toString() : '2'
                    }
                },
                immediate: true
            }
        },
        template: /*html*/`
     <div data-scm-order class="scm-order-detail-wrap">
        <advance-detail v-if="orderTypeCode==='0'"/>
        <ordinary-detail v-if="orderTypeCode==='2'"/>
        <store-detail v-if="orderTypeCode==='1'"/>
    </div>
    `
    });
});
