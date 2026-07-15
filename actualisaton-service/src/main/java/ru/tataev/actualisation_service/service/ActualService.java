package ru.tataev.actualisation_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tataev.actualisation_service.dto.CheckRequestDto;
import ru.tataev.actualisation_service.dto.CheckResponseDto;
import ru.tataev.actualisation_service.dto.ItemRequestDto;
import ru.tataev.actualisation_service.dto.ItemResponseDto;
import ru.tataev.actualisation_service.entity.*;
import ru.tataev.actualisation_service.exception.BusinessLogicException;
import ru.tataev.actualisation_service.exception.InvalidRequestException;
import ru.tataev.actualisation_service.exception.ResourceNotFoundException;
import ru.tataev.actualisation_service.repository.ItemsRepository;
import ru.tataev.actualisation_service.repository.OrderRepository;
import ru.tataev.actualisation_service.repository.StatusRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActualService {
    private final ItemsRepository itemsRepository;
    private final StatusRepository statusRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public CheckResponseDto checkStatus(CheckRequestDto req){
        if (req == null) {
            throw new InvalidRequestException("Пришел пустой запрос");
        }

        List<ItemResponseDto> itemsDtoList = new ArrayList<>();
        for (ItemRequestDto itemDto: req.getItems()){
            validateCount(itemDto.getCount());

            Items item = itemsRepository.findById(itemDto.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Товара с id " + itemDto.getId() + " нет в базе"));

            if (item.getQuantity() > 0){
                Status statusToSet = statusRepository.findById(ItemsStatus.ITEM_AVAILABLE.getId())
                                .orElseThrow(()-> new ResourceNotFoundException("Статус " + ItemsStatus.ITEM_AVAILABLE.getName() + " не найден"));

                item.setStatus(statusToSet);
                itemsRepository.save(item);
            }
            else{
                Status statusToSet = statusRepository.findById(ItemsStatus.ITEM_OUT_OF_STACK.getId())
                        .orElseThrow(()-> new ResourceNotFoundException("Статус " + ItemsStatus.ITEM_OUT_OF_STACK.getName() + " не найден"));

                item.setStatus(statusToSet);
                itemsRepository.save(item);
            }

            ItemResponseDto itemResDto = new ItemResponseDto();
            itemResDto.setId(String.valueOf(item.getId()));
            itemResDto.setCount(item.getQuantity());
            itemResDto.setStatus(String.format("%04d", item.getStatus().getId()));

            itemsDtoList.add(itemResDto);
        }

        CheckResponseDto res = new CheckResponseDto();
        res.setItems(itemsDtoList);
        res.setIdWarehouse(req.getIdWarehouse());
        res.setDate(req.getDate());
        res.setIdBasket(req.getIdBasket());
        res.setIdStatus(req.getIdStatus());
        res.setIdUser(req.getIdUser());
        return res;
    }

    @Transactional
    public CheckResponseDto changeCount(CheckRequestDto req){
        if (req == null) {
            throw new InvalidRequestException("Пришел пустой запрос");
        }

        List<ItemResponseDto> itemsDtoList = new ArrayList<>();

        Order order = orderRepository.findById(req.getIdBasket())
                .orElseThrow(()-> new ResourceNotFoundException("Корзина с id " + req.getIdBasket() + " не найдена"));

        for (ItemRequestDto itemDto: req.getItems()){
            validateCount(itemDto.getCount());

            Items item = itemsRepository.findById(itemDto.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Товара с id " + itemDto.getId() + " нет в базе"));

            ItemResponseDto itemResDto = new ItemResponseDto();

            if (itemDto.getCount() > 0){
                if (item.getStatus().getId().equals(ItemsStatus.ITEM_AVAILABLE.getId())){
                    if (item.getQuantity() - itemDto.getCount() >= 0){
                        itemResDto.setStatus(String.format("%04d", item.getStatus().getId()));
                        item.setQuantity(item.getQuantity() - itemDto.getCount());
                        itemsRepository.save(item);
                    }
                    else {
                        throw new BusinessLogicException("Товара c id " + itemDto.getId() + " нет в наличии");
                    }
                }
                else {
                    throw new BusinessLogicException("Товара c id " + itemDto.getId() + " нет в наличии");
                }
            }
            else {
                if (order.getStatus().getId().equals(OrderStatus.CANCELED.getId())){
                    item.setQuantity(item.getQuantity() + itemDto.getCount()*(-1));

                    Status statusToSet = statusRepository.findById(ItemsStatus.ITEM_AVAILABLE.getId())
                            .orElseThrow(()-> new ResourceNotFoundException("Статус " + ItemsStatus.ITEM_AVAILABLE.getName() + " не найден"));
                    item.setStatus(statusToSet);
                    itemResDto.setStatus(String.format("%04d", item.getStatus().getId()));
                    itemsRepository.save(item);
                }
                else {
                    throw new ResourceNotFoundException("Товары на отмену не найдены");
                }
            }


            itemResDto.setId(String.valueOf(item.getId()));
            itemResDto.setCount(item.getQuantity());

            itemsDtoList.add(itemResDto);
        }

        CheckResponseDto res = new CheckResponseDto();
        res.setItems(itemsDtoList);
        res.setIdWarehouse(req.getIdWarehouse());
        res.setDate(req.getDate());
        res.setIdBasket(req.getIdBasket());
        res.setIdStatus(order.getStatus().getId());
        res.setIdUser(req.getIdUser());
        return res;
    }

    public void validateCount(Integer count){
        if (count == null || count == 0){
            throw new InvalidRequestException("Все значения count в items должны быть не равны 0");
        }
    }
}
