package one.digitalinnovation.parking.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import one.digitalinnovation.parking.repository.ParkingRepository;
import org.springframework.stereotype.Service;

import one.digitalinnovation.parking.exception.ParkingNotFoundException;
import one.digitalinnovation.parking.model.Parking;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingService {

	private final ParkingRepository parkingRepository;

	public ParkingService(ParkingRepository parkingRepository) {
		this.parkingRepository = parkingRepository;
	}

	private static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional(readOnly = true)
	public List<Parking> findAll() {
		return parkingRepository.findAll();
    }

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Parking findById(String id) {
		return parkingRepository
				.findById(id)
				.orElseThrow(() -> new ParkingNotFoundException(id));
    }
	@Transactional
    public Parking create(Parking parking) {
		String uuid = getUUID();
		parking.setId(uuid);
		parking.setEntryDate(LocalDateTime.now());
		parkingRepository.save(parking);
		return parking;
    }

	@Transactional
    public void delete(String id) {
		findById(id);
		parkingRepository.deleteById(id);
    }

	@Transactional
    public Parking update(String id, Parking parkingCreate) {
		Parking parking = findById(id);
		parking.setColor(parkingCreate.getColor());
		parking.setState(parkingCreate.getState());
		parking.setModel(parkingCreate.getModel());
		parking.setLicense(parkingCreate.getLicense());
		parkingRepository.save(parking);
		return parking;
    }

	@Transactional
	public Parking checkOut(String id) {
		Parking parking = findById(id);
		parking.setExitDate(LocalDateTime.now());
		parking.setBill(ParkingCheckOut.getBill(parking));
		parkingRepository.save(parking);
		return parking;
	}


}
