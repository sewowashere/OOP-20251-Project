--- model Paketi ---
Encapsulation kuralına göre tüm değişkenler private olmalıdır.
	Plane.java: Uçak ID, model, kapasite ve koltuk dizilimini (seatMatrix) tutar.
	Flight.java: Sefer numarası, kalkış/varış noktası ve uçuş süresi gibi temel verileri saklar.
	Seat.java: Koltuk numarası, sınıfı (Enum: ECONOMY/BUSINESS), fiyatı ve rezervasyon durumunu (boolean) içerir.
	Passenger.java: Yolcu kimlik ve iletişim bilgilerini barındırır.
	Reservation.java: Bir yolcu, bir uçuş ve bir koltuk nesnesini eşleştiren ana nesnedir.
	Ticket.java & Baggage.java: Kesinleşmiş bilet ve bagaj ağırlığı bilgilerini tutar.

--- service (veya manager) Paketi ---
	FlightManager.java: Uçuş ekleme, silme ve güncelleme işlemlerini yapar.
	SeatManager.java: Koltuk düzenini oluşturur ve boş koltukları hesaplar (JUnit testi zorunludur).
	ReservationManager.java: En kritik sınıf. Aynı anda (multithreading) yapılan 90 rezervasyonu, çakışma olmadan (synchronized) yönetmekle sorumludur.
	CalculatePrice.java: Bilet fiyatlarını sınıf bazlı (ECONOMY/BUSINNES) hesaplar.

--- db Paketi ---
	FileHandler.java: Veritabanı yasak olduğu için , tüm verileri .txt dosyalarına kalıcı olarak yazmak ve okumaktan sorumludur.

--- gui Paketi ---
	LoginScreen.java, MainScreen.java, AdminScreen.java: Kullanıcı etkileşimini sağlar. Rapor oluşturma sırasında ekranın donmaması için Thread kullanmalıdır.

--- test Paketi ---
	SystemTest.java: En az 5 adet JUnit testi (fiyat hesaplama, koltuk sayısı kontrolü, uçuş arama vb.) içermelidir.


src/
├── db/
│   └── FileHandler.java             // Kalıcı veri depolama işlemleri (Txt dosyaları) [cite: 47, 48]
│
├── models/                          // Veri Sınıfları (Entity/POJO)
│   ├── Flight.java                  // Uçuş bilgilerini tutar
│   ├── Plane.java                   // PlaneID, model, seatMatrix
│   ├── Route.java                   // Kalkış/Varış bilgileri
│   ├── Seat.java                    // seatNum, Class(Enum), price, status
│   ├── Passenger.java               // Yolcu bilgileri
│   ├── Reservation.java             // Rezervasyon kodu ve nesne ilişkileri
│   ├── Baggage.java                 // Ağırlık bilgisi, Ticket ile ilişkili
│   ├── Ticket.java                  // Abstract Class (Inheritance için) [cite: 4, 12]
│   ├── EconomyTicket.java           // Ticket'tan türeyen alt sınıf [cite: 4, 34]
│   └── BusinessTicket.java          // Ticket'tan türeyen alt sınıf [cite: 4, 34]
│
├── service/                         // Abstraction (Interface) Katmanı
│   ├── IFlightService.java          // FlightManager için sözleşme
│   ├── IReservationService.java     // ReservationManager için sözleşme
│   └── ISeatService.java            // SeatManager için sözleşme
│
├── managers/                        // İş Mantığı (Business Logic) [cite: 13]
│   ├── FlightManager.java           // Uçuş oluşturma, güncelleme, silme [cite: 15]
│   ├── ReservationManager.java      // Rezervasyon yapma/iptal + Concurrency (Synchronized) [cite: 15, 18, 19]
│   ├── SeatManager.java             // Düzen oluşturma, müsaitlik hesaplama [cite: 15]
│   ├── CalculatePrice.java          // Fiyat hesaplama mantığı (JUnit için zorunlu) [cite: 15, 33]
│   └── ReportGenerator.java         // Asenkron raporlama (Thread) [cite: 27, 29]
│
├── gui/                             // Görsel Arayüz (Swing veya JavaFX) [cite: 41, 42]
│   ├── LoginScreen.java             // Kullanıcı/Personel girişi [cite: 43]
│   ├── FlightBookingScreen.java     // Arama, koltuk seçimi ve rezervasyon [cite: 44]
│   ├── ReservationAdminScreen.java  // Rezervasyon sorgulama ve iptal [cite: 45]
│   └── AdminStaffScreen.java        // Uçuş yönetimi ve personel işlemleri [cite: 45]
│
└── test/                            // JUnit 5 Testleri (En az 5 adet) [cite: 31, 32]
    ├── PriceCalculationTest.java    // Ekonomi/Business fiyat kontrolü [cite: 33, 34]
    ├── FlightSearchEngineTest.java  // Filtreleme ve zaman kontrolü [cite: 35, 36, 37]
    └── SeatManagerTest.java         // Koltuk sayısı ve hata (exception) testleri [cite: 38, 39, 40]