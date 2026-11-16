
package com.harika.inventorymanagementsystem.PurchaseInvoiceServiceTest;

import com.harika.inventorymanagementsystem.entity.Product;
import com.harika.inventorymanagementsystem.entity.Stock;
import com.harika.inventorymanagementsystem.exception.InsufficientStockException;
import com.harika.inventorymanagementsystem.repository.ProductRepository;
import com.harika.inventorymanagementsystem.repository.StockRepository;
import com.harika.inventorymanagementsystem.service.StockService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockService stockService;


    // TEST 1: Increase existing stock

    @Test
    void shouldIncreaseExistingStock() {

        // Arrange
        Stock existingStock = new Stock();
        existingStock.setQuantity(5);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(existingStock));

        when(stockRepository.save(any(Stock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Stock updatedStock = stockService.increaseStock(1L, 8);

        // Assert
        assertEquals(13, updatedStock.getQuantity());

        // Verify
        verify(stockRepository, times(1))
                .findByProductId(1L);

        verify(stockRepository, times(1))
                .save(any(Stock.class));

        verify(productRepository, never())
                .findById(1L);
    }


    // TEST 2: Create new stock

    @Test
    void shouldCreateNewStockWhenStockDoesNotExist() {

        // Arrange
        Product product = new Product();

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.empty());

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(stockRepository.save(any(Stock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Stock newStock = stockService.increaseStock(1L, 8);

        // Assert
        assertNotNull(newStock);
        assertEquals(8, newStock.getQuantity());
        assertEquals(product, newStock.getProduct());

        // Verify
        verify(stockRepository, times(1))
                .findByProductId(1L);

        verify(productRepository, times(1))
                .findById(1L);

        verify(stockRepository, times(1))
                .save(any(Stock.class));
    }


    // TEST 3: Product not found

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        // Arrange
        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.empty());

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stockService.increaseStock(1L, 8)
        );

        assertEquals("Product not found", exception.getMessage());

        // Verify
        verify(stockRepository, times(1))
                .findByProductId(1L);

        verify(productRepository, times(1))
                .findById(1L);

        verify(stockRepository, never())
                .save(any(Stock.class));
    }


    // TEST 4: Received quantity is zero

    @Test
    void shouldThrowExceptionWhenReceivedQuantityIsZero() {

        // Arrange
        Integer receivedQuantity = 0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.increaseStock(1L, receivedQuantity)
        );

        assertEquals(
                "Received quantity must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verify(stockRepository, never())
                .findByProductId(1L);

        verify(productRepository, never())
                .findById(1L);

        verify(stockRepository, never())
                .save(any(Stock.class));
    }


    // TEST 5: Received quantity is negative

    @Test
    void shouldThrowExceptionWhenReceivedQuantityIsNegative() {

        // Arrange
        Integer receivedQuantity = -5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.increaseStock(1L, receivedQuantity)
        );

        assertEquals(
                "Received quantity must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verify(stockRepository, never())
                .findByProductId(1L);

        verify(productRepository, never())
                .findById(1L);

        verify(stockRepository, never())
                .save(any(Stock.class));
    }


    // TEST 6: Received quantity is null

    @Test
    void shouldThrowExceptionWhenReceivedQuantityIsNull() {

        // Arrange
        Integer receivedQuantity = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.increaseStock(1L, receivedQuantity)
        );

        assertEquals(
                "Received quantity must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verify(stockRepository, never())
                .findByProductId(1L);

        verify(productRepository, never())
                .findById(1L);

        verify(stockRepository, never())
                .save(any(Stock.class));
    }


    // TEST 7: Existing stock quantity is negative

    @Test
    void shouldThrowExceptionWhenExistingStockQuantityIsNegative() {

        // Arrange
        Stock existingStock = new Stock();
        existingStock.setQuantity(-5);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(existingStock));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.increaseStock(1L, 8)
        );

        assertEquals(
                "Stock quantity is invalid",
                exception.getMessage()
        );

        // Verify
        verify(stockRepository, times(1))
                .findByProductId(1L);

        verify(stockRepository, never())
                .save(any(Stock.class));

        verify(productRepository, never())
                .findById(1L);
    }


    // TEST 8: Existing stock quantity is null

    @Test
    void shouldThrowExceptionWhenExistingStockQuantityIsNull() {

        // Arrange
        Stock existingStock = new Stock();
        existingStock.setQuantity(null);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(existingStock));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.increaseStock(1L, 8)
        );

        assertEquals(
                "Stock quantity is invalid",
                exception.getMessage()
        );

        // Verify
        verify(stockRepository, times(1))
                .findByProductId(1L);

        verify(stockRepository, never())
                .save(any(Stock.class));

        verify(productRepository, never())
                .findById(1L);
    }



    // TEST 9: Decrease existing stock (13 -> 9)
    @Test
    void shouldDecreaseExistingStock() {

        // Arrange
        Stock stock = new Stock();
        stock.setQuantity(13);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(stock));

        when(stockRepository.save(any(Stock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Stock updatedStock = stockService.decreaseStock(1L, 4);

        // Assert
        assertNotNull(updatedStock);
        assertEquals(9, updatedStock.getQuantity());

        // Verify
        verify(stockRepository, times(1)).findByProductId(1L);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }


    // TEST 10: Sell all available stock (10 -> 0)
    @Test
    void shouldAllowSellingAllAvailableStock() {

        // Arrange
        Stock stock = new Stock();
        stock.setQuantity(10);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(stock));

        when(stockRepository.save(any(Stock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Stock updatedStock = stockService.decreaseStock(1L, 10);

        // Assert
        assertNotNull(updatedStock);
        assertEquals(0, updatedStock.getQuantity());

        // Verify
        verify(stockRepository, times(1)).findByProductId(1L);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }


    // TEST 11: Product ID is null
    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {

        // Arrange
        Long productId = null;

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.decreaseStock(productId, 4)
        );

        assertEquals(
                "Product ID must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verifyNoInteractions(stockRepository);
    }


    // TEST 12: Product ID is zero
    @Test
    void shouldThrowExceptionWhenProductIdIsZero() {

        // Arrange
        Long productId = 0L;

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.decreaseStock(productId, 4)
        );

        assertEquals(
                "Product ID must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verifyNoInteractions(stockRepository);
    }


    // TEST 13: Product ID is negative
    @Test
    void shouldThrowExceptionWhenProductIdIsNegative() {

        // Arrange
        Long productId = -1L;

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.decreaseStock(productId, 4)
        );

        assertEquals(
                "Product ID must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verifyNoInteractions(stockRepository);
    }


    // TEST 14: Sold quantity is null
    @Test
    void shouldThrowExceptionWhenSoldQuantityIsNull() {

        // Arrange
        Integer soldQuantity = null;

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.decreaseStock(1L, soldQuantity)
        );

        assertEquals(
                "Sold quantity must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verifyNoInteractions(stockRepository);
    }


    // TEST 15: Sold quantity is zero
    @Test
    void shouldThrowExceptionWhenSoldQuantityIsZero() {

        // Arrange
        Integer soldQuantity = 0;

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.decreaseStock(1L, soldQuantity)
        );

        assertEquals(
                "Sold quantity must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verifyNoInteractions(stockRepository);
    }


    // TEST 16: Sold quantity is negative
    @Test
    void shouldThrowExceptionWhenSoldQuantityIsNegative() {

        // Arrange
        Integer soldQuantity = -4;

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.decreaseStock(1L, soldQuantity)
        );

        assertEquals(
                "Sold quantity must be greater than zero",
                exception.getMessage()
        );

        // Verify
        verifyNoInteractions(stockRepository);
    }


    // TEST 17: Stock not found
    @Test
    void shouldThrowExceptionWhenStockIsNotFound() {

        // Arrange
        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stockService.decreaseStock(1L, 4)
        );

        assertEquals("Stock not found", exception.getMessage());

        // Verify
        verify(stockRepository, times(1)).findByProductId(1L);
        verify(stockRepository, never()).save(any(Stock.class));
    }


    // TEST 18: Existing stock quantity is null
    @Test
    void shouldThrowExceptionWhenExistingStockQuantityIsNullWhenDecreasingStock() {

        // Arrange
        Stock stock = new Stock();
        stock.setQuantity(null);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(stock));

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.decreaseStock(1L, 4)
        );

        assertEquals("Stock quantity is invalid", exception.getMessage());

        // Verify
        verify(stockRepository, times(1)).findByProductId(1L);
        verify(stockRepository, never()).save(any(Stock.class));
    }


    // TEST 19: Existing stock quantity is negative
    @Test
    void shouldThrowExceptionWhenExistingStockQuantityIsNegativeWhenDecreasingStock() {

        // Arrange
        Stock stock = new Stock();
        stock.setQuantity(-5);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(stock));

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.decreaseStock(1L, 4)
        );

        assertEquals("Stock quantity is invalid", exception.getMessage());

        // Verify
        verify(stockRepository, times(1)).findByProductId(1L);
        verify(stockRepository, never()).save(any(Stock.class));
    }


    // TEST 20: Sold quantity greater than available stock
    @Test
    void shouldThrowExceptionWhenSoldQuantityIsGreaterThanAvailableStock() {

        // Arrange
        Stock stock = new Stock();
        stock.setQuantity(10);

        when(stockRepository.findByProductId(1L))
                .thenReturn(Optional.of(stock));

        // Act + Assert
        InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> stockService.decreaseStock(1L, 12)
        );

        assertEquals(
                "Insufficient stock. Available: 10, Requested: 12",
                exception.getMessage()
        );

        // Verify
        verify(stockRepository, times(1)).findByProductId(1L);
        verify(stockRepository, never()).save(any(Stock.class));
    }

}
