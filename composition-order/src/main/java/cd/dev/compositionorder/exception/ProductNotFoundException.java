package cd.dev.compositionorder.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Producto no encontrado con ID: " + productId);
    }
}
