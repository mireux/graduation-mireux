import Request from '@/utils/request'

export function getOrderByUser() {
  return Request({
    url: '/order/getAll',
    method: 'get'
  })
}

export function cancelOrder(param) {
  return Request({
    url: '/order/cancel?bookOrderId='+param,
    method: 'post'
  })
}
