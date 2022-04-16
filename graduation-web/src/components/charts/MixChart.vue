<template>
  <div :id="id" :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'
import { getInfo, getRoom } from '@/api/showInfo'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    id: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '200px'
    },
    height: {
      type: String,
      default: '200px'
    }
  },
  data() {
    return {
      chart: null,
      inData: [],
      outData: [],
      valueData: [],
      chosenSeat: [],
      totalSeat: [],
      room: []
    }
  },
  created() {
    this.getRoom()
    this.getInfo()
  },
  mounted() {
    this.initChart()
  },
  watch: {
    seriesData(val) {
      this.chart.setOption({ series: val })
    }
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    getRoom() {
      return new Promise((resolve, reject) => {
        getRoom().then(resp => {
          const { data } = resp
          this.valueData = data.room
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    getInfo() {
      return new Promise((resolve, reject) => {
        getInfo().then(resp => {
          const { data } = resp
          this.dataList = data
          this.inData = data.chosenSeat
          console.log(this.inData)
          this.outData = data.totalSeat
          console.log(this.outData)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    initChart() {
      this.chart = echarts.init(document.getElementById(this.id))

      const xData = (function() {
        const data = ['自习室2', '自习室1']
        // for (let i = 0; i < this.valueData.length; i++) {
        //   data.push(this.valueData[i])
        // }
        return data
      }())
      // const xData = this.valueData
      // console.log(this.valueData)
      // console.log(xData)
      this.chart.setOption({
        backgroundColor: '#344b58',
        title: {
          text: '使用详情',
          x: '20',
          top: '20',
          textStyle: {
            color: '#fff',
            fontSize: '22'
          },
          subtextStyle: {
            color: '#90979c',
            fontSize: '16'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            textStyle: {
              color: '#fff'
            }
          }
        },
        grid: {
          left: '5%',
          right: '5%',
          borderWidth: 0,
          top: 150,
          bottom: 95,
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          x: '5%',
          top: '10%',
          textStyle: {
            color: '#90979c'
          },
          data: ['已使用', '总数']
        },
        calculable: true,
        xAxis: [{
          type: 'category',
          axisLine: {
            lineStyle: {
              color: '#90979c'
            }
          },
          splitLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          splitArea: {
            show: false
          },
          axisLabel: {
            interval: 0

          },
          data: xData
        }],
        yAxis: [{
          type: 'value',
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#90979c'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            interval: 0
          },
          splitArea: {
            show: false
          }
        }],
        dataZoom: [{
          show: true,
          height: 30,
          xAxisIndex: [
            0
          ],
          bottom: 30,
          start: 0,
          end: 80,
          handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
          handleSize: '110%',
          handleStyle: {
            color: '#d3dee5'

          },
          textStyle: {
            color: '#fff'
          },
          borderColor: '#90979c'

        }, {
          type: 'inside',
          show: true,
          height: 15,
          start: 1,
          end: 35
        }],
        series: [{
          name: '已使用',
          type: 'bar',
          stack: 'total',
          barMaxWidth: 35,
          barGap: '10%',
          itemStyle: {
            normal: {
              color: 'rgba(255,144,128,1)',
              label: {
                show: true,
                textStyle: {
                  color: '#fff'
                },
                position: 'insideTop',
                formatter(p) {
                  return p.value > 0 ? p.value : ''
                }
              }
            }
          },
          data: [0, 4]
        },

          {
            name: '总数',
            type: 'bar',
            stack: 'total',
            itemStyle: {
              normal: {
                color: 'rgba(0,191,183,1)',
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: 'top',
                  formatter(p) {
                    return p.value > 0 ? p.value : ''
                  }
                }
              }
            },
            data: [25, 30]
          }
          // {
          //   name: '支出',
          //   type: 'line',
          //   stack: 'total',
          //   symbolSize: 10,
          //   symbol: 'circle',
          //   itemStyle: {
          //     normal: {
          //       color: 'rgba(252,230,48,1)',
          //       barBorderRadius: 0,
          //       label: {
          //         show: true,
          //         position: 'top',
          //         formatter(p) {
          //           return p.value > 0 ? p.value : ''
          //         }
          //       }
          //     }
          //   },
          //   data: this.valueData
          // }
        ]
      })
    }
  }
}
</script>
