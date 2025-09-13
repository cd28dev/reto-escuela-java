package cd.dev.stockms.mapper;

import cd.dev.stockms.model.dto.FindByProductIdDto;
import cd.dev.stockms.model.dto.StockCreateRequestDto;
import cd.dev.stockms.model.dto.StockResponseDto;
import cd.dev.stockms.model.dto.StockUpdateRequestDto;
import cd.dev.stockms.model.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface StockMapper {

    Stock toEntity(StockCreateRequestDto dto);

    Stock toEntity(StockUpdateRequestDto dto);

    StockResponseDto toResponseDto(Stock entity);

    List<StockResponseDto> toResponseDtoList(List<Stock> entities);


    @Mapping(target = "total", source = "total")
    FindByProductIdDto toFindByProductIdDto(Long productId, Integer total);
}