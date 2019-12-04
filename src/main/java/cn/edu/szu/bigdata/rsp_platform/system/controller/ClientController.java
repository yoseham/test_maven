package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.oauth.client.Client;
import cn.edu.szu.bigdata.rsp_platform.oauth.client.ClientService;
import cn.edu.szu.bigdata.rsp_platform.oauth.configuration.ResourceServerConfiguration;
import cn.edu.szu.bigdata.rsp_platform.system.model.ClientParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Api(value = "客户端管理", tags = "authorities")
@RequestMapping("${api.version}/oauth/client")
@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;


    @BussinessLog(operEvent = "查询所有客户端",operType = 1)
    @ApiOperation(value = "查询所有客户端")
    @PreAuthorize("hasAuthority('get:/v1/oauth/client')")
    @GetMapping()
    public JsonResult list(String keyword) {
        List<Client> clientList = clientService.findAll();
        // 筛选结果
        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            Iterator<Client> iterator = clientList.iterator();
            while (iterator.hasNext()) {
                Client next = iterator.next();
                boolean b = (next.getClientName() != null && next.getClientName().contains(keyword));
                if (!b) {
                    iterator.remove();
                }
            }
        }
        return JsonResult.ok().put("data", clientList);
    }

    @BussinessLog(operEvent = "添加客户端",operType = 1)
    @ApiOperation(value = "添加客户端")
    @PreAuthorize("hasAuthority('post:/v1/oauth/client')")
    @PostMapping()
    public JsonResult add(@RequestBody ClientParam param) {
        param.populateDefault();
        Client client = new Client(
                UUID.randomUUID().toString(),
                ResourceServerConfiguration.RESOURCE_NAME,
                param.getScope(),
                param.getRedirectUri(),
                param.getClientName()
        );
        clientService.addClientDetails(client);
        return JsonResult.ok("添加成功");
    }

    @BussinessLog(operEvent = "根据clientId查询",operType = 1)
    @ApiOperation(value = "根据clientId查询")
    @PreAuthorize("hasAuthority('get:/v1/oauth/client/{clientId}')")
    @GetMapping("/{clientId}")
    public JsonResult get(@PathVariable String clientId) {
        return JsonResult.ok().put("data", clientService.loadClientByClientId(clientId));
    }

    @BussinessLog(operEvent = "修改客户端",operType = 1)
    @ApiOperation(value = "修改客户端")
    @PreAuthorize("hasAuthority('put:/v1/oauth/client/{clientId}')")
    @PutMapping("/{clientId}")
    public JsonResult update(@PathVariable String clientId, @RequestBody ClientParam param) {
        Client client = clientService.findByClientId(clientId);
        if (client == null) {
            throw new NoSuchClientException("客户端不存在");
        }
        param.populateDefault();
        if (!StringUtils.isEmpty(param.getClientName())) {
            client.setClientName(param.getClientName());
        }
        if (param.getRedirectUri() != null) {
            client.setRegisteredRedirectUri(param.getRedirectUri());
        }

        if (param.getScope() != null) {
            client.setScope(param.getScope());
        }
        clientService.updateClientDetails(client);
        return JsonResult.ok("修改成功");
    }

    @BussinessLog(operEvent = "删除客户端",operType = 1)
    @ApiOperation(value = "删除客户端")
    @PreAuthorize("hasAuthority('delete:/v1/oauth/client/{clientId}')")
    @DeleteMapping("/{clientId}")
    public JsonResult delete(@PathVariable String clientId) {
        clientService.removeClientDetails(clientId);
        return JsonResult.ok("删除成功");
    }
}
