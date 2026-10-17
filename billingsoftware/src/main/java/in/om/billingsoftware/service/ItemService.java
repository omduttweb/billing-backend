package in.om.billingsoftware.service;

import in.om.billingsoftware.io.ItemRequest;
import in.om.billingsoftware.io.ItemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    ItemResponse add(ItemRequest request, MultipartFile file);

    List<ItemResponse> fetchItems();

    void deleteItems(String itemId);
}
