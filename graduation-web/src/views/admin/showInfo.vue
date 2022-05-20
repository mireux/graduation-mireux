<!--  在Echarts.vue文件中 -->
<template>

  <div class="Echarts">
    <div id="main"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getInfo, getRoom } from '@/api/showInfo'

export default {
  name: 'Monthly',
  data() {
    return {
      goodsName: '',
      dataList: {chosenSeat:[],totalSeat:[]},
      chosenSeat: [],
      totalSeat: [],
      roomList: [],
      seriesList: []
    }
  },
  methods: {
    myEcharts() {
      var chartDom = document.getElementById('main')
      var myChart = echarts.init(chartDom)
      var option

      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // Use axis to trigger tooltip
            type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
          }
        },
        legend: {},
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: this.roomList
        },
        series: this.seriesList
      }
      option && myChart.setOption(option)
    },
    getRoom() {
      return new Promise((resolve, reject) => {
        getRoom().then(resp => {
          const { data } = resp
          this.roomList = data
          this.myEcharts()
          console.log(this.roomList)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取所有的数据
    getAllData() {
      return new Promise((resolve, reject) => {
        getInfo().then(resp => {
          const { data } = resp
          this.dataList = data
          this.totalSeat = this.dataList.totalSeat
          this.chosenSeat = this.dataList.chosenSeat
          let series1 = {
            name: '全部座位',
            type: 'bar',
            stack: 'total',
            label: {
              show: true
            },
            emphasis: {
              focus: 'series'
            },
            data: this.totalSeat
          }
          let series2 = {
            name: '已选座位',
            type: 'bar',
            stack: 'total',
            label: {
              show: true
            },
            emphasis: {
              focus: 'series'
            },
            data: this.chosenSeat
          }
          this.seriesList.push(series1)
          this.seriesList.push(series2)
          this.myEcharts()
          console.log(this.roomList)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },
  mounted() {
    this.getAllData()
  },
  created() {
    this.getRoom()
  }
}
</script>

<style>
#main {
  width: 1200px;
  height: 500px;
  margin-left: 5%;
  margin-top: 20px;
  /*background: aquamarine;*/
}
</style>

