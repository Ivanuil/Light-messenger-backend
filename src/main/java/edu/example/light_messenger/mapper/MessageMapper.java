package edu.example.light_messenger.mapper;

import edu.example.light_messenger.dto.MessageResponseDto;
import edu.example.light_messenger.model.MessageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "to.username", target = "to")
    MessageResponseDto toMessageResponseDto(MessageModel messageModel);

}
