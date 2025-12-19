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
