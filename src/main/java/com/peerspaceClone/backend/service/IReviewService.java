package com.peerspaceClone.backend.service;

import java.util.List;
import com.peerspaceClone.backend.dto.ReviewInsertDTO;
import com.peerspaceClone.backend.dto.ReviewReadOnlyDTO;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;

public interface IReviewService {
    ReviewReadOnlyDTO saveReview(ReviewInsertDTO dto) throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException;
    List<ReviewReadOnlyDTO> getReviewsByPropertyId(Long propertyId) throws EntityNotFoundException;
    List<ReviewReadOnlyDTO> getReviewsByRevieweeId(Long revieweeId) throws EntityNotFoundException;
    ReviewReadOnlyDTO deleteReviewById(Long id) throws EntityNotFoundException;
}
