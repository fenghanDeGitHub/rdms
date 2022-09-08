import request from '@/utils/request'

// 查询传真列表
export function list(query) {
  return request({
    url: '/msm/message/list',
    method: 'post',
    data: query
  })
}

// 查询传真详细
export function getMsmInfoById(sendInfoId) {
  return request({
    url: '/msm/message/getInfoById' + sendInfoId,
    method: 'get'
  })
}
