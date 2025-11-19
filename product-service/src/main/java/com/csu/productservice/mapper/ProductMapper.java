package com.csu.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.productservice.entity.Product;
import com.csu.productservice.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository()
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
