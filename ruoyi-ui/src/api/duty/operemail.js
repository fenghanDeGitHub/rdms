import request from '@/utils/request'

// 查询邮件列表
export function list(query) {
  return request({
    url: '/msm/message/list',
    method: 'post',
    data: query
  })
}

// 查询邮件详细
export function getMsmInfoById(sendInfoId) {
  return request({
    url: '/msm/message/getInfoById' + sendInfoId,
    method: 'get'
  })
}
