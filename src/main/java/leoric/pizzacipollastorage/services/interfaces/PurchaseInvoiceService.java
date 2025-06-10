package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PurchaseInvoiceService {
    PurchaseInvoiceResponseDto createInvoice(PurchaseInvoiceCreateDto dto);

    PurchaseInvoiceResponseDto getById(UUID id);

    List<PurchaseInvoiceResponseDto> getLatestInvoices(int limit);
}