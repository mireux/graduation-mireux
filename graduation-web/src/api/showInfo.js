import request from '@/utils/request'

export function getInfo() {
  return request({
    url: '/info/admin/get',
    method: 'get'
  })
}

export function getRoom() {
  return request({
    url: '/info/admin/get/room',
    method: 'get'
  })
}
