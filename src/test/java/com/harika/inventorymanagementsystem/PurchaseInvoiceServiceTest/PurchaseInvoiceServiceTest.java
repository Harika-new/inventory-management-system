package com.harika.inventorymanagementsystem.PurchaseInvoiceServiceTest;


import com.harika.inventorymanagementsystem.entity.Product;
import com.harika.inventorymanagementsystem.entity.PurchaseInvoice;
import com.harika.inventorymanagementsystem.entity.PurchaseItem;
import com.harika.inventorymanagementsystem.repository.PurchaseInvoiceRepository;
import com.harika.inventorymanagementsystem.service.PurchaseInvoiceService;
import com.harika.inventorymanagementsystem.service.StockService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseInvoiceServiceTest {

    @Mock
    private PurchaseInvoiceRepository purchaseInvoiceRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private PurchaseInvoiceService purchaseInvoiceService;

    @Test
    void shouldCalculatePurchaseTotalAndIncreaseReceivedStock() {

        // Arrange: Create dummy product
        Product product = new Product();
        product.setId(1L);

        // Arrange: Create dummy purchase item
        PurchaseItem item = new PurchaseItem();
        item.setProduct(product);
        item.setQuantity(10);
        item.setReceivedQuantity(8);
        item.setPurchasePrice(new BigDecimal("300"));

        // Arrange: Create dummy purchase invoice
        PurchaseInvoice invoice = new PurchaseInvoice();
        invoice.setPurchaseItems(List.of(item));

        // Mock repository save operation
        when(purchaseInvoiceRepository.save(invoice))
                .thenReturn(invoice);

        // Act: Execute the service method
        PurchaseInvoice savedInvoice =
                purchaseInvoiceService.savePurchaseInvoice(invoice);

        // Assert: Check invoice total
        assertEquals(//checks the actual result against the expected result.
                new BigDecimal("2400"),
                savedInvoice.getTotalAmount()
        );
        //checks whether a dependency method was called with the expected arguments.
        // Verify: Check stock increase request
        verify(stockService).increaseStock(1L, 8);
    }
}