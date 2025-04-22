package com.demo.service;

import com.demo.dto.CreateFavouriteRequest;
import com.demo.dto.FavouriteResponse;
import com.demo.entity.Favourite;
import com.demo.repository.FavouriteRepository;

public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;

    @Override
    public FavouriteResponse getFavouriteByMemberId(Integer memberId) {
        return favouriteRepository.findByMemberId(memberId);
    }

    @Override
    public FavouriteResponse createFavourite(CreateFavouriteRequest request) {
        Favourite favourite = new Favourite();
        favourite.setMemberId(request.getMemberId);
        favourite.setAttractionId(request.getAttractionId());
        favourite.setCreatedAt(request.getCreatedAt());

        return mapToResponse(favouriteRepository.save(favourite));

    }

    @Override
    public void deleteFavourite(Long id) {
        favouriteRepository.delete(id);
    }

    private FavouriteResponse mapToResponse(Favourite favourite) {
        FavouriteResponse response = new FavouriteResponse();
        response.setId(favourite.getId());
        response.setMemberId(favourite.getMemberId());
        response.setAttractionId(favourite.getAttractionId());
        response.setCreatedAt(favourite.getCreatedAt());
        return response;
    }
}
