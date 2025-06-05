package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PurchaseInvoiceService {
    PurchaseInvoiceResponseDto createInvoice(PurchaseInvoiceCreateDto dto);

    PurchaseInvoiceResponseDto getById(Long id);

    List<PurchaseInvoiceResponseDto> getLatestInvoices(int limit);
}