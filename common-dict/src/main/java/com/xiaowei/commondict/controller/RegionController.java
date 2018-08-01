package com.xiaowei.commondict.controller;

import com.xiaowei.commondict.dto.RegionDTO;
import com.xiaowei.commondict.entity.Region;
import com.xiaowei.commondict.query.RegionQuery;
import com.xiaowei.commondict.region.RegionUtils;
import com.xiaowei.commondict.service.IRegionService;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaohuaiying
 * @Date 2017-09-01  13:43
 * @Description行政区域控制器
 * @Version 1.0
 */
@Api(tags = "行政区域")
@RestController
@RequestMapping("/api/region")
public class RegionController {

    @Autowired
    private IRegionService regionService;

    @ApiOperation(value = "根据code获取子地域")
    @GetMapping("/child")
    public Result getChild(String code, FieldsView fieldsView) {
        List<Region> regions = regionService.findChildrenByParentCode(code);
        return Result.getSuccess(ObjectToMapUtils.listToMap(regions, fieldsView));
    }



    @ApiOperation(value = "行政区域查询")
    @GetMapping("")
    @SuppressWarnings("all")
    public Result query(RegionQuery regionQuery, FieldsView fieldsView) throws Exception {
        if(!StringUtils.isEmpty(regionQuery.getParentCode())){
            Region region = regionService.findByCode(RegionUtils.completed(regionQuery.getParentCode()));
            if(region != null){
                regionQuery.setParentCode(region.getShortCode());
            }
        }

        if (regionQuery.isNoPage()) {
            List<Region> regions = regionService.query(regionQuery,Region.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(regions, fieldsView));
        } else {
            PageResult pageResult = regionService.queryPage(regionQuery, Region.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }


    @ApiOperation(value = "通过id查询区域")
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") String id, FieldsView fieldsView) {
        Region region = regionService.findById(id);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(region, fieldsView));
    }

    @ApiOperation(value = "添加区域")
    @AutoErrorHandler
    @PostMapping("")
    public Result save(@RequestBody RegionDTO regionDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Region region = BeanCopyUtils.copy(regionDTO, Region.class);
        region = regionService.saveRegion(region);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(region, fieldsView));
    }

    @ApiOperation(value = "编辑区域")
    @AutoErrorHandler
    @PutMapping("/{regionId}")
    public Result update(@PathVariable("regionId") String regionId, @RequestBody @Validated(V.Update.class) RegionDTO regionDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Region region = BeanCopyUtils.copy(regionDTO, Region.class);
        region.setId(regionId);
        region = regionService.updateRegion(region);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(region, fieldsView));
    }

    @ApiOperation(value = "删除区域")
    @DeleteMapping("/{regionId}")
    public Result delete(@PathVariable("regionId") String regionId) throws Exception {
        regionService.deleteRegion(regionId);
        return Result.getSuccess();
    }

}