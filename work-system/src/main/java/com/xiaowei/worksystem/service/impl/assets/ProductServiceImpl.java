package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.assets.Product;
import com.xiaowei.worksystem.service.assets.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product> implements IProductService {

    public ProductServiceImpl(@Qualifier("productRepository") BaseRepository repository) {
        super(repository);
    }

}