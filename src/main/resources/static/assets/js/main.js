/** EasyWeb spa v3.1.2 date:2019-06-05 License By http://easyweb.vip */

layui.config({
    version: true,
    base: 'assets/module/'
}).extend({
    formSelects: 'formSelects/formSelects-v4',
    treetable: 'treetable-lay/treetable',
    dropdown: 'dropdown/dropdown',
    notice: 'notice/notice',
    step: 'step-lay/step',
    dtree: 'dtree/dtree',
    citypicker: 'city-picker/city-picker',
    tableSelect: 'tableSelect/tableSelect',
    cropper: 'cropper/cropper',
    zTree: 'zTree/zTree'
}).use(['config', 'layer', 'element', 'index', 'admin', 'laytpl'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var element = layui.element;
    var config = layui.config;
    var index = layui.index;
    var admin = layui.admin;
    var laytpl = layui.laytpl;

    // 检查是否登录
    if (!config.getToken()) {
        return location.replace('login.html');
    }

    // 获取用户信息
    admin.req('user/info', {}, function (res) {
        admin.removeLoading();  // 移除页面加载动画
        if (200 == res.code) {
            config.putUser(res.user);
            admin.renderPerm();  // 移除没有权限的元素
            $('#huName').text(res.user.nickName);
        } else {
            layer.msg('获取用户失败', {icon: 2});
        }
    }, 'get');

    // 加载侧边栏
    admin.req('user/menu', {}, function (res) {
        laytpl(sideNav.innerHTML).render(res.data, function (html) {
            $('.layui-layout-admin .layui-side .layui-nav').html(html);
            element.render('nav');
        });
        index.regRouter(res.data);  // 注册路由
        index.loadHome({  // 加载主页
            url: '#/welcome',
            name: '<i class="layui-icon layui-icon-home"></i>'
        });
    }, 'get');

});
