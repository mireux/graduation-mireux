import Request from '@/utils/request'

export function getRoomList(param) {
  return Request({
      url: '/room/getAll?isScreen='+param,
      method: 'get'
    }
  )
}

export function InsertRoom(data) {
  return Request({
    url: '/room/insert',
    method: 'post',
    data
  })
}

export function disabledTheRoom(param) {
  return Request({
    url: '/room/disabled?id='+param,
    method: 'post',
  })
}

export function EditTheRoom(id,data) {
  return Request({
    url: '/room/edit?id='+id,
    method: 'post',
    data
  })
}

export function bookTheSeat(data) {
  return Request({
    url: '/seat/book',
    method: 'post',
    data
  })
}

