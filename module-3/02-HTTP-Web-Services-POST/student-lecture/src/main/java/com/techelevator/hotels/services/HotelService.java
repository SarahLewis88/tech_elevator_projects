package com.techelevator.hotels.services;

import com.techelevator.hotels.models.Hotel;
import com.techelevator.hotels.models.Reservation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class HotelService {

	private final String BASE_URL;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ConsoleService console = new ConsoleService();

	public HotelService(String url) {
		BASE_URL = url;
	}

	/**
	 * Create a new reservation in the hotel reservation system
	 *
	 * @param newReservation
	 * @return Reservation
	 */
	public Reservation addReservation(String newReservation) {
		// TODO: Implement method
		Reservation reservation = makeReservation(newReservation);

		if (reservation == null) {
			return null;
		}

		HttpHeaders headers = new HttpHeaders(); // this creates an object that represents the HTTP Header, Utility
													// class
		headers.setContentType(MediaType.APPLICATION_JSON); // server api will document required/optional header
															// information

		HttpEntity<Reservation> entity = new HttpEntity(reservation, headers);

		reservation = restTemplate.postForObject(BASE_URL + "hotels/" + reservation.getHotelID() + "/reservations", entity, Reservation.class); //This is a method (postForObject) that takes in perameters
//																																^^^tells server what data-type data should be returned as	
		return reservation;
	}

	/**
	 * Updates an existing reservation by replacing the old one with a new
	 * reservation
	 *
	 * @param CSV
	 * @return
	 */
	public Reservation updateReservation(String CSV) {
		Reservation reservation = makeReservation(CSV);
		if (reservation == null) {
			return null;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		HttpEntity<Reservation> entity = new HttpEntity<>(reservation, headers);
		
		restTemplate.put(BASE_URL + "reservations/" + reservation.getId(), entity);
		
		return reservation;
	}

	/**
	 * Delete an existing reservation
	 *
	 * @param id
	 */
	public void deleteReservation(int id) {
		// TODO: Implement method
	}

	/**
	 * List all hotels in the system
	 *
	 * @return
	 */
	public Hotel[] listHotels() {
		// TODO: Implement method
		return null;
	}

	/**
	 * Get the details for a single hotel by id
	 *
	 * @param id
	 * @return Hotel
	 */
	public Hotel getHotel(int id) {
		// TODO: Implement method
		return null;
	}

	/* DON'T MODIFY ANY METHODS BELOW */
	/**
	 * List all reservations in the hotel reservation system
	 *
	 * @return Reservation[]
	 */
	public Reservation[] listReservations() {
		Reservation[] reservations = null;
		try {
			reservations = restTemplate.getForObject(BASE_URL + "reservations", Reservation[].class);
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return reservations;
	}

	/**
	 * List all reservations by hotel id.
	 *
	 * @param hotelId
	 * @return Reservation[]
	 */
	public Reservation[] listReservationsByHotel(int hotelId) {
		Reservation[] reservations = null;
		try {
			reservations = restTemplate.getForObject(BASE_URL + "hotels/" + hotelId + "/reservations",
					Reservation[].class);
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return reservations;
	}

	/**
	 * Find a single reservation by the reservationId
	 *
	 * @param reservationId
	 * @return Reservation
	 */
	public Reservation getReservation(int reservationId) {
		Reservation reservation = null;
		try {
			reservation = restTemplate.getForObject(BASE_URL + "reservations/" + reservationId, Reservation.class);
		} catch (RestClientResponseException ex) {
			console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			console.printError(ex.getMessage());
		}
		return reservation;
	}

	private Reservation makeReservation(String CSV) {
		String[] parsed = CSV.split(",");

		// invalid input (
		if (parsed.length < 5 || parsed.length > 6) {
			return null;
		}

		// Add method does not include an id and only has 5 strings
		if (parsed.length == 5) {
			// Create a string version of the id and place into an array to be concatenated
			String[] withId = new String[6];
			String[] idArray = new String[] { new Random().nextInt(1000) + "" };
			// place the id into the first position of the data array
			System.arraycopy(idArray, 0, withId, 0, 1);
			System.arraycopy(parsed, 0, withId, 1, 5);
			parsed = withId;
		}

		return new Reservation(Integer.parseInt(parsed[0].trim()), Integer.parseInt(parsed[1].trim()), parsed[2].trim(),
				parsed[3].trim(), parsed[4].trim(), Integer.parseInt(parsed[5].trim()));
	}

}
