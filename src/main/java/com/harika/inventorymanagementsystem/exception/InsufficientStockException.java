package com.harika.inventorymanagementsystem.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
//The key question is:
//
//Why can't InsufficientStockException itself give a proper HTTP response to Postman?
//
//Because the exception represents the problem. We need another component to decide what HTTP response the client should receive.
//
//        That's the job of GlobalExceptionHandler.
//
//We'll learn @RestControllerAdvice first, then @ExceptionHandler, then create the class.

//You were asking:
//
//        > **“Why did we first create `InsufficientStockException`, and now suddenly create `GlobalExceptionHandler`? What is the difference?”**
//
//Here is the simple answer:
//
//        ### `InsufficientStockException`
//
//This represents **the problem**.
//
//        ```text
//        Stock = 6
//Customer wants = 10
//        ↓
//InsufficientStockException
//```
//
//It basically says:
//
//        > **“There is an insufficient-stock problem.”**
//
//        ### `GlobalExceptionHandler`
//
//This handles **what we should send back to the client** when that problem occurs.
//
//        ```text
//        InsufficientStockException
//        ↓
//GlobalExceptionHandler
//        ↓
//HTTP response
//        ↓
//Postman / Frontend
//```
//
//So:
//
//        > **Exception = What went wrong?**
//        > **Handler = What should we do/send when it goes wrong?**
//
//        ### Why we need both
//
//Think of a shop:
//
//        ```text
//Problem:
//        "No stock available"
//
//        ↓
//Exception
//
//Response:
//        "Sorry, only 6 pieces are available."
//
//        ↓
//Exception Handler
//```
//
//So we didn't create two classes for the same job.
//
//        ```text
//        InsufficientStockException
//→ identifies the specific problem
//
//GlobalExceptionHandler
//→ handles that problem and creates the client response
//```
//
//Now the sequence makes sense:
//
//        **Custom Exception → throw it → Handler catches/handles it → client receives proper response.**
