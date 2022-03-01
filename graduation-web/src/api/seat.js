import request from '@/utils/request'

export function getSeatByRoomId(id) {
  return request({
    url: '/seat/getList?roomId='+id,
    method: 'get'
  })
}

export function changeSeatStatus(id) {
  return request({
    url: '/seat/status/change?seatId='+id,
    method: 'post'
  })
}

export function lockSeat(id) {
  return request({
    url: '/seat/lock?seatId='+id,
    method: 'post'
  })
}
