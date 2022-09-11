<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="邮箱类型" prop="mailType">
        <el-select
          v-model="queryParams.mailType"
          placeholder="请选择"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="item in mailTypeData"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="mailName">
        <el-input
          v-model="queryParams.mailName"
          placeholder="请输入邮件标题"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入发送地址"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数量" prop="fileSize">
        <el-input
          v-model="queryParams.fileSize"
          placeholder="请输入附件数量"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="邮件编号" align="center" prop="messageId" />
      <el-table-column label="邮箱类型" align="center" prop="mailType" />
      <el-table-column label="标题" align="center" prop="mailName" />
      <el-table-column label="附件数量" align="center" prop="fileSize" />
      <el-table-column label="发送地址" align="center" prop="address" :show-overflow-tooltip="true" />
      <el-table-column label="接收时间" align="center" prop="receivedDate" sortable="custom" :sort-orders="['descending', 'ascending']" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.receivedDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 邮件详细 -->
    <el-dialog title="邮件详细" :visible.sync="open" width="700px" append-to-body>
      <el-descriptions class="margin-top" :column="2">
        <el-descriptions-item label="发送时间" v-if="form.createTime">{{ form.createTime }}</el-descriptions-item>
        <el-descriptions-item label="发送单位" v-if="form.theElectricityUnit">{{ form.theElectricityUnit }}</el-descriptions-item>
        <el-descriptions-item label="发送电话" v-if="form.tel">{{ form.tel }}</el-descriptions-item>
        <el-descriptions-item label="发件人" v-if="form.userName">
          {{ form.userName }}
        </el-descriptions-item>
        <el-descriptions-item label="接收时间" v-if="form.telTime">{{ form.telTime }}</el-descriptions-item>
        <el-descriptions-item label="接收电话" v-if="form.phone">{{ form.phone }}</el-descriptions-item>
        <el-descriptions-item label="接收人" v-if="form.answerPeople">{{ form.answerPeople }}</el-descriptions-item>
        <el-descriptions-item label="邮件标题" v-if="form.title">{{ form.title }}</el-descriptions-item>
        <el-descriptions-item label="邮件内容" v-if="form.content">{{ form.content }}</el-descriptions-item>
        <el-descriptions-item label="处理意见" v-if="form.suggestion">{{ form.suggestion }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, getMsmInfoById } from "@/api/duty/operemail";

export default {
  name: "operemail",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 默认排序
      defaultSort: {prop: 'receivedDate', order: 'descending'},
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mailName: undefined,
        address: undefined,
        mailType: undefined,
        fileSize: undefined
      },
      mailTypeData: [{
        value: '163',
        label: '163邮箱'
      },{
        value: 'pearlwater',
        label: '珠江委邮箱'
      }]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询邮件列表 */
    getList() {
      this.loading = true;
      list({ MsmParam: this.queryParams }).then( response => {
      // list(this.addDateRange(this.queryParams, this.dateRange)).then( response => {
          console.log(response)
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.operId)
      this.multiple = !selection.length
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /** 详细按钮操作 */
    handleView(sendInfoId) {
      // getMsmInfoById(sendInfoId).then( res => {
      //     this.form = res.data;
      //     this.open = true;
      //   }
      // );
      this.open = true;
      this.form = sendInfoId;
    }
  }
};
</script>