lyfdefine([], function () {
    return ({
        computed: {
            currentTag () {
                const tag = this.tags.find(n => n.value === this.tagValue)
                if(tag){
                    return tag
                } else {
                    return this.tags[0]
                }
            }
        },
        props: {
            tagValue: {
                type: String,
                default: '001'
            }
        },
        data() {
            return {
                tags: [{
                    bgc: '#108ee9',
                    label: '未',
                    value: '001'
                },
                    {
                        bgc: '#17b3a3',
                        label: '退',
                        value: '002'
                    },
                    {
                        bgc: '#F56C6C',
                        label: '补',
                        value: '003'
                    },
                    {
                        bgc: '#2db7f5',
                        label: '子',
                        value: '004'
                    },
                    {
                        bgc: '#87d068',
                        label: '组',
                        value: '005'
                    }
                ]
            }
        },
        template: /*html*/`
 <el-tag size="small"  :color="currentTag.bgc"><span style="color:#fff;">{{currentTag.label}}</span></el-tag>
    `
    });
});
