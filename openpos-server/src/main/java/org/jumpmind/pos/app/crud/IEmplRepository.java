package org.jumpmind.pos.app.crud;

import org.jumpmind.pos.app.model.Empl;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IEmplRepository extends PagingAndSortingRepository<Empl, Long> {

}
