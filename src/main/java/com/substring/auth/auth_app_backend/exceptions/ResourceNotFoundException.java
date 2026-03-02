package com.substring.auth.auth_app_backend.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(){
        super("Resource Not Found !!");
    }
}


//2️⃣ Checked vs Unchecked Exceptions (Core Difference)
//Feature	                                Checked Exception	                        Unchecked Exception
//Checked at compile time?	                        ✅ Yes	                                ❌ No
//Must handle with try-catch or throws?	            ✅ Mandatory	                        ❌ Optional
//Extend which class?	                            Exception (but NOT RuntimeException)	RuntimeException
//Represent	                                         Recoverable conditions	                   Programming errors



//Checked Exception Types
//1. IOException
//2. SQLException
//3. FileNotFoundException
//4. ParseException


// Common Unchecked Exceptions
//1. NullPointerException
//2. IllegalArgumentException
//3. ArithmeticException
//4. IndexOutOfBoundsException
//5. IllegalStateException


//What does super(message) do?
//It calls the parent class constructor (RuntimeException). Since RuntimeException already knows how to:
//1. Store the error message
//2. Store the cause
//3. Manage stack trace
//4. Handle exception chaining
//We delegate that responsibility to it.

