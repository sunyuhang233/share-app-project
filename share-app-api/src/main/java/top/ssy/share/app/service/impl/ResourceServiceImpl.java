package top.ssy.share.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ssy.share.app.common.cache.TokenStoreCache;
import top.ssy.share.app.common.result.PageResult;
import top.ssy.share.app.convert.ResourceConvert;
import top.ssy.share.app.dto.ResourcePublishDTO;
import top.ssy.share.app.entity.Resource;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.entity.UserAction;
import top.ssy.share.app.enums.ResourceStatusEnum;
import top.ssy.share.app.enums.UserActionEnum;
import top.ssy.share.app.mapper.ResourceMapper;
import top.ssy.share.app.mapper.UserMapper;
import top.ssy.share.app.query.ResourceQuery;
import top.ssy.share.app.query.UserActionResourceQuery;
import top.ssy.share.app.service.CategoryService;
import top.ssy.share.app.service.ResourceService;
import top.ssy.share.app.service.TagService;
import top.ssy.share.app.service.UserActionService;
import top.ssy.share.app.vo.ResourceDetailVO;
import top.ssy.share.app.vo.ResourceListItemVO;
import top.ssy.share.app.vo.UserActionListInfo;

import java.util.List;
import java.util.stream.Collectors;

import static top.ssy.share.app.common.constant.Constant.NO_TOKEN;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : impl
 * @create : 2024-03-05 12:40
 **/

@Slf4j
@Service
@AllArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    private final TokenStoreCache tokenStoreCache;
    private final UserMapper userMapper;
    private final UserActionService userActionService;
    private final TagService tagService;
    private final CategoryService categoryService;

    @Override
    @Options(useGeneratedKeys = true, keyProperty = "pk_id")
    @Transactional(rollbackFor = Exception.class)
    public void publish(ResourcePublishDTO dto, String accessToken) {
        log.info("ResourceServiceImpl.publish dto:{}", dto);
        Integer userId = tokenStoreCache.getUser(accessToken).getPkId();
        Resource resource = ResourceConvert.INSTANCE.convert(dto);
        resource.setAuthor(userId);
        resource.setStatus(ResourceStatusEnum.UNAUDITED.getCode());
        resource.setLikeNum(0);
        log.info("ResourceServiceImpl.publish resource:{}", resource);
        save(resource);
        // 发布就插入记录，如果审核不通过，删除记录
        userActionService.insertUserAction(userId, resource.getPkId(), UserActionEnum.PUBLISH);
    }

    @Override
    public PageResult<ResourceListItemVO> getResourcePage(ResourceQuery query) {
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resource::getStatus, ResourceStatusEnum.AUDITED.getCode())
                .like(StringUtils.isNotBlank(query.getKeyword()), Resource::getTitle, "%" + query.getKeyword() + "%")
                .like(StringUtils.isNotBlank(query.getKeyword()), Resource::getDetail, "%" + query.getKeyword() + "%")
                .eq(query.getDiskType() != null && query.getDiskType() > 0, Resource::getDiskType, query.getDiskType())
                .apply(query.getResType() != null && query.getResType() > 0, "JSON_CONTAINS(res_type,JSON_ARRAY({0}))=1", query.getResType())
                .apply(query.getTagId() != null && query.getTagId() > 0, "JSON_CONTAINS(tags,JSON_ARRAY({0}))=1", query.getTagId())
                .orderByDesc(Resource::getIsTop)
                .orderByDesc(Resource::getCreateTime);
        Page<Resource> page = page(new Page<>(query.getPage(), query.getLimit()), wrapper);
        List<Resource> records = page.getRecords();
        List<ResourceListItemVO> voList = records.stream().map(resource -> {
            ResourceListItemVO vo = new ResourceListItemVO();
            vo.setPkId(resource.getPkId());
            vo.setTitle(resource.getTitle());
            vo.setPrice(resource.getPrice());
            vo.setIsTop(resource.getIsTop());
            vo.setDetail(resource.getDetail());
            vo.setCreateTime(resource.getCreateTime());

            User author = userMapper.selectById(resource.getAuthor());
            vo.setAuthor(author.getNickname());
            vo.setAuthorAvatar(author.getAvatar());
            vo.setDiskType(categoryService.getById(resource.getDiskType()).getTitle());
            vo.setResType(categoryService.queryCategoryNameList(resource.getResType()));
            vo.setTags(tagService.queryTagNamesByIds(resource.getTags()));
            vo.setLikeNum(userActionService.selectCountByResourceIdAndType(resource.getPkId(), UserActionEnum.LIKE));
            vo.setDownloadNum(userActionService.selectCountByResourceIdAndType(resource.getPkId(), UserActionEnum.EXCHANGE));
            vo.setCollectNum(userActionService.selectCountByResourceIdAndType(resource.getPkId(), UserActionEnum.COLLECT));

            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(voList, page.getTotal());
    }

    @Override
    public ResourceDetailVO getResourceDetail(Integer resourceId, String accessToken) {
        Resource resource = getById(resourceId);
        ResourceDetailVO detail = new ResourceDetailVO();
        detail.setPkId(resource.getPkId());
        detail.setTitle(resource.getTitle());
        detail.setPrice(resource.getPrice());
        detail.setIsTop(resource.getIsTop());
        detail.setDownloadUrl(resource.getDownloadUrl());
        detail.setDetail(resource.getDetail());
        detail.setRemark(resource.getRemark());
        detail.setStatus(resource.getStatus());
        detail.setCreateTime(resource.getCreateTime());

        User author = userMapper.selectById(resource.getAuthor());
        detail.setAuthor(author.getNickname());
        detail.setAuthorAvatar(author.getAvatar());
        detail.setDiskType(categoryService.getById(resource.getDiskType()).getTitle());
        detail.setResType(categoryService.queryCategoryNameList(resource.getResType()));
        detail.setTags(tagService.queryTagNamesByIds(resource.getTags()));
        detail.setLikeNum(userActionService.selectCountByResourceIdAndType(resourceId, UserActionEnum.LIKE));
        detail.setDownloadNum(userActionService.selectCountByResourceIdAndType(resourceId, UserActionEnum.EXCHANGE));
        detail.setCollectNum(userActionService.selectCountByResourceIdAndType(resourceId, UserActionEnum.COLLECT));

        if (!accessToken.equals(NO_TOKEN)) {
            Integer currentUserId = tokenStoreCache.getUser(accessToken).getPkId();
            detail.setIsLike(userActionService.resourceIsAction(currentUserId, resourceId, UserActionEnum.LIKE));
            detail.setIsCollect(userActionService.resourceIsAction(currentUserId, resourceId, UserActionEnum.COLLECT));
            detail.setIsDownload(userActionService.resourceIsAction(currentUserId, resourceId, UserActionEnum.EXCHANGE));
        }

        return detail;
    }

    @Override
    public PageResult<ResourceListItemVO> getUserActionResourcePage(UserActionResourceQuery query, String accessToken) {
        Integer userId = tokenStoreCache.getUser(accessToken).getPkId();
        UserActionListInfo userActionListInfo = userActionService
                .selectResourceListByUserIdAndType(userId, UserActionEnum.getByCode(query.getType()), new Page<UserAction>(query.getPage(), query.getLimit()));
        if (userActionListInfo.getTotal() == 0) {
            return new PageResult<>(null, 0);
        }
        List<Resource> records = baseMapper.selectBatchIds(userActionListInfo.getResourceIdList());
        List<ResourceListItemVO> voList = records.stream().map(resource -> {
            ResourceListItemVO vo = new ResourceListItemVO();
            vo.setPkId(resource.getPkId());
            vo.setTitle(resource.getTitle());
            vo.setPrice(resource.getPrice());
            vo.setIsTop(resource.getIsTop());
            vo.setDetail(resource.getDetail());
            vo.setStatus(resource.getStatus());
            vo.setCreateTime(resource.getCreateTime());

            User author = userMapper.selectById(resource.getAuthor());
            vo.setAuthor(author.getNickname());
            vo.setAuthorAvatar(author.getAvatar());
            vo.setDiskType(categoryService.getById(resource.getDiskType()).getTitle());
            vo.setResType(categoryService.queryCategoryNameList(resource.getResType()));
            vo.setTags(tagService.queryTagNamesByIds(resource.getTags()));
            vo.setLikeNum(userActionService.selectCountByResourceIdAndType(resource.getPkId(), UserActionEnum.LIKE));
            vo.setDownloadNum(userActionService.selectCountByResourceIdAndType(resource.getPkId(), UserActionEnum.EXCHANGE));
            vo.setCollectNum(userActionService.selectCountByResourceIdAndType(resource.getPkId(), UserActionEnum.COLLECT));

            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(voList, userActionListInfo.getTotal());
    }

}
