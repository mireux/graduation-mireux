<!--  在Echarts.vue文件中 -->
<template xmlns="http://www.w3.org/1999/html">

  <div class="Echarts">
    <div id="main"></div>
    <div>
      您总共预约了{{dataList.bookTotalTimes}}次,总共学习时长为{{dataList.totalStudyTime}}分钟，超越了{{dataList.totalTimesRating * 100}}%的人<br>
      您上周预约了{{dataList.bookWeekTimes}}次，超越了{{dataList.weekTimesRating * 100}}%的人
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getStudentData } from '@/api/showInfo'

export default {
  name: 'StudyReport',
  data() {
    return {
      optionsData: [],
      dataList: [],
      lastStudyTime: 0
    }
  },
  methods: {
    myEcharts() {
      var chartDom = document.getElementById('main')
      var myChart = echarts.init(chartDom)
      var option
      this.changeData()
      option = {
        title: {
          text: '月累计学习时长',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}({d}%)' /*饼状图触碰之后显示的注释文字*/
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: '50%',
            data: this.optionsData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      option && myChart.setOption(option)
    },
    // 处理数据
    changeData() {
      console.log(this.lastStudyTime)
      this.optionsData = [
        {
          value: this.lastStudyTime,
          name: '学习时长'
        },
        {
          value: 465 - this.lastStudyTime,
          name: '未学习时长'
        }
      ]
    },
    getData() {
      return new Promise((resolve, reject) => {
        getStudentData().then(resp => {
          const { data } = resp
          this.dataList = data
          console.log(this.dataList)
          this.lastStudyTime = this.dataList.lastMonthStudyTime
          this.myEcharts()
        }).catch(error => {
          reject(error)
        })
      })
    }

  },
  mounted() {
    this.getData()
  },
  created() {
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

