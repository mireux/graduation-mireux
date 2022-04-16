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

export function changePassword(data) {
  return request({
    url: '/user/changePass',
    method: 'post',
    data
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/user/info/update',
    method: 'post',
    data
  })
}

export function getAllUser() {
  return request({
    url: '/user/getAll',
    method: 'get'
  })
}

export function changeTheStatus(id, status) {
  return request({
    url: '/user/status/change?id=' + id + '&status=' + status,
    method: 'post'
  })
}

export function deleteUserById(id) {
  return request({
    url: '/user/delete?id=' + id,
    method: 'post'
  })
}

export function insertUser(data) {
  return request({
    url: '/user/insert',
    method: 'post',
    data
  })
}


