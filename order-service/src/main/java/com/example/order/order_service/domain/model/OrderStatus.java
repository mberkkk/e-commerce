package com.example.order.order_service.domain.model;

public enum OrderStatus {
    PENDING_PAYMENT,   // Ödeme bekleniyor (Sipariş oluşturulduktan sonra)
    PROCESSING,        // Sipariş işleniyor (Ödeme alındı, stok ayrıldı)
    CONFIRMED,         // Sipariş onaylandı (Tüm kontroller başarılı)
    SHIPPED,           // Kargoya verildi
    DELIVERED,         // Teslim edildi
    CANCELLED,         // İptal edildi (Müşteri veya yönetici tarafından)
    FAILED,            // Başarısız (Ödeme/Stok vb. sorunlar nedeniyle)
    REFUNDED           // Para iadesi yapıldı
}