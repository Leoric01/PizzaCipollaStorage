package leoric.pizzacipollastorage.purchase.services;

import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PurchaseInvoiceService {
    PurchaseInvoiceResponseDto createInvoice(UUID branchId, PurchaseInvoiceCreateDto dto);

    PurchaseInvoiceResponseDto getById(UUID branchId, UUID id);

    void stockFromInvoice(UUID branchId, UUID invoiceId);

    List<PurchaseInvoiceResponseDto> getAll(UUID branchId);

    List<PurchaseInvoiceResponseDto> getLatestInvoices(UUID branchId, int limit);

    PurchaseInvoiceResponseDto update(UUID branchId, UUID invoiceId, PurchaseInvoiceCreateDto dto);

    void delete(UUID branchId, UUID invoiceId);
}