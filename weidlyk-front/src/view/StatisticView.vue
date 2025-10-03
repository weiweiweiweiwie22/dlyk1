<script setup>
import { doGet } from "../http/httpRequest.js";
import { onMounted, reactive, ref } from "vue";
import * as echarts from 'echarts';

const data = reactive({
  statisticData: {},
  funnelData: [],
  sourceData: [],
})

const loadFunnelChartData = async () => {
  try {
    const res = await doGet('/api/statistic/funnel');
    if (res.data.code === 200) {
      data.funnelData = res.data.data;
    }
  } catch (error) {
    console.error("加载漏斗图数据失败:", error);
  }
}

const loadSourcePieChartData = async () => {
  try {
    const res = await doGet('/api/statistic/pie');
    if (res.data.code === 200) {
      data.sourceData = res.data.data;
    }
  } catch (error) {
    console.error("加载渠道图数据失败:", error);
  }
}

const loadSourcePieChart = async () => {
  await loadSourcePieChartData();

  const chartDom = document.getElementById('sourcePieChart');
  const myChart = echarts.init(chartDom);
  let option;

  option = {
    title: {
      text: '线索来源',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'horizontal',
      left: 'center'
    },
    series: [
      {
        name: '线索来源',
        type: 'pie',
        radius: '50%',
        // data: [
        //   { value: 1048, name: 'Search Engine' },
        //   { value: 735, name: 'Direct' },
        //   { value: 580, name: 'Email' },
        //   { value: 484, name: 'Union Ads' },
        //   { value: 300, name: 'Video Ads' }
        // ],
        data: data.sourceData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  option && myChart.setOption(option);

}
const loadData = () => {
  doGet('/api/statistic/total').then(res => {
    if (res.data.code === 200) {
      data.statisticData = res.data.data;
    }
  })
}

const loadFunnelChart = async () => {
  await loadFunnelChartData();

  const chartDom = document.getElementById('saleFunnelChart');
  if (!chartDom) return;

  const myChart = echarts.init(chartDom);
  let option;

  option = {
    // 【核心修改 1】: 将 title 移至底部
    title: {
      text: '销售漏斗图',
      left: 'center', // 水平居中
      top: 'bottom',  // 垂直底部
      textStyle: {
        fontSize: 16
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c}'
    },
    toolbox: {
      feature: {
        dataView: { readOnly: false },
        restore: {},
        saveAsImage: {}
      }
    },
    // 【核心修改 2】: 将 legend 移至顶部
    legend: {
      orient: 'horizontal', // 水平排列
      top: 'top', // 垂直顶部
      data: data.funnelData.map(item => item.name)
    },
    series: [
      {
        name: '销售漏斗数据统计',
        type: 'funnel',
        // 【核心修改 3】: 调整图表边距，避免拥挤
        left: '10%',
        top: 40,      // 顶部留出空间给 legend
        bottom: 40,   // 底部留出空间给 title
        width: '80%',
        sort: 'descending',
        gap: 2,
        label: {
          show: true,
          position: 'inside'
        },
        labelLine: {
          length: 10,
          lineStyle: {
            width: 1,
            type: 'solid'
          }
        },
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 1
        },
        emphasis: {
          label: {
            fontSize: 20
          }
        },
        data: data.funnelData
      }
    ]
  };

  option && myChart.setOption(option);
}

onMounted(() => {
  loadData();
  loadFunnelChart();
  loadSourcePieChart();
})
</script>

<template>
  <el-row :gutter="16">
    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4">
      <el-statistic :value="data.statisticData.effectiveActivityCount">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            市场活动
          </div>
        </template>
        <template #suffix>/{{ data.statisticData.totalActivityCount }}</template>
      </el-statistic>
    </el-col>

    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4">
      <el-statistic title="线索总数" :value="data.statisticData.totalClueCount" />
    </el-col>

    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4">
      <el-statistic title="客户总数" :value="data.statisticData.totalCustomerCount" />
    </el-col>

    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4">
      <el-statistic :value="data.statisticData.successTranAmount">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            交易总额
          </div>
        </template>
        <template #suffix>/{{ data.statisticData.totalTranAmount }}</template>
      </el-statistic>
    </el-col>

  </el-row>

  <div id="saleFunnelChart" style="width: 48%; height:350px; margin: 10px auto; float: left;">
  </div>

  <div id="sourcePieChart" style="width: 48%; height:350px; margin: 10px auto; float: left;"></div>
</template>

<style scoped>
.el-row{
  text-align: center;
}
</style>