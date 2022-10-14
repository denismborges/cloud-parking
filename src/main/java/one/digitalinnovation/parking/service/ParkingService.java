package one.digitalinnovation.parking.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import one.digitalinnovation.parking.exception.ParkingNotFoundException;
import one.digitalinnovation.parking.model.Parking;

@Service
public class ParkingService {

    private static Map<String, Parking> parkingMap = new HashMap<String, Parking>();

    static {
	String id = getUUID();
	String id1 = getUUID();
	
	Parking parking = new Parking(id, "DMS-1111", "SC", "CELTA", "PRETO");
	parkingMap.put(id, parking);
		
	Parking parking1 = new Parking(id1, "WAS-1234", "SP", "VW GOL", "VERMELHO");
	parkingMap.put(id1, parking1);
    }

    private static String getUUID() {
	return UUID.randomUUID().toString().replace("-", "");
    }

    public List<Parking> findAll() {
	return parkingMap.values()
		.stream()
		.collect(Collectors.toList());
    }

    public Parking findById(String id) {
	Parking parking = parkingMap.get(id);
	
	if (parking == null) {
	    throw new ParkingNotFoundException(id);
	}
	
	return parking;
    }

    public Parking create(Parking parking) {
	String uuid = getUUID();
	parking.setId(uuid);
	parking.setEntryDate(LocalDateTime.now());
	parkingMap.put(uuid, parking);
	return parking;
    }

    public void delete(String id) {
	findById(id);
	parkingMap.remove(id);
    }

    public Parking update(String id, Parking parkingCreate) {
	Parking parking = findById(id);
	parking.setColor(parkingCreate.getColor());
	parkingMap.replace(id, parking);
	return parking;
    }


}
