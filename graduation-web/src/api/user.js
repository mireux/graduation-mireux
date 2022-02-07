import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function UpdateRoleInfo(data) {
  return request({
    url: '/user/info/update',
    method: 'post',
    data
  })
}

export function UpdateLoad() {
  return request({
    url: '/user/avatar/update',
    method: 'post'
  })
}

export function CancelUpdateAvatar() {
  return request({
    url: '/user/avatar/cancel',
    method: 'post'
  })
}
