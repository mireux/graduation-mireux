<template>
  <div class="warning">
    <div>
      <div id="attention">
        <h2><i class="el-icon-warning"/>&nbsp;&nbsp;注意事项</h2>
        <ol>
          <li>请将手机等电子产品调整为静音状态；</li>
          <li>学习区只能带入饮用水，其余（食物、饮料）一律不准带入；</li>
          <li>尽量使用静音键盘与鼠标</li>
          <li>学习区禁止交谈、接打电话，如有需要请移步至图书馆外；</li>
          <li>不得在自习室任意位置涂鸦；</li>
          <li>如需归还图书将图书放至图书归还处</li>
          <li>还有任何其余问题，可以咨询图书馆志愿者或者管理员</li>
        </ol>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'warning',

  data() {
    return {}
  }
}
</script>

<style scoped>
#title {
  text-align: center;
  color: #303133;
}

#attention {
  margin-top: 100px;
  margin-left: 300px;
  user-select: none;
}

ol {
  margin-top: 50px;
}

ol li {
  font-size: 20px;
  margin-top: 20px;

}
</style>
