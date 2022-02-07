import request from '@/utils/request'

export function getSeatByRoomId(id) {
  return request({
    url: '/seat/getList?roomId='+id,
    method: 'get'
  })
}

