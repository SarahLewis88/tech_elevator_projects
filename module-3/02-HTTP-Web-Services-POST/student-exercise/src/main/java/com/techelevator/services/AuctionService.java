package com.techelevator.services;

import com.techelevator.models.Auction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AuctionService {

	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	private final ConsoleService console = new ConsoleService();

	public AuctionService(String url) {
		BASE_URL = url;
	}

	public Auction[] getAll() {
		Auction[] auctions = null;
		try {
			auctions = restTemplate.getForObject(BASE_URL, Auction[].class);
		} catch (RestClientResponseException ex) {
			console.printError("Could not retrieve the auctions. Is the server running?");
		} catch (ResourceAccessException ex) {
			console.printError("A network error occurred.");
		}
		return auctions;
	}

	public Auction getOne(int id) {
		Auction auction = null;
		try {
			auction = restTemplate.getForObject(BASE_URL + "/" + id, Auction.class);
		} catch (RestClientResponseException ex) {
			console.printError("Could not retrieve the auction.");
		} catch (ResourceAccessException ex) {
			console.printError("A network error occurred.");
		}
		return auction;
	}

	public Auction[] getByTitle(String title) {
		Auction[] auctions = null;
		try {
			auctions = restTemplate.getForObject(BASE_URL + "?title_like=" + title, Auction[].class);
		} catch (RestClientResponseException ex) {
			console.printError("The title was not found. Please try again.");
		} catch (ResourceAccessException ex) {
			console.printError("A network error occurred.");
		}
		return auctions;
	}

	public Auction[] getByPrice(double price) {
		Auction[] auctions = null;
		try {
			auctions = restTemplate.getForObject(BASE_URL + "?currentBid_lte=" + price, Auction[].class);
		} catch (RestClientResponseException ex) {
			console.printError("No auctions found. Please try again.");
		} catch (ResourceAccessException ex) {
			console.printError("A network error occurred.");
		}
		return auctions;
	}

	public Auction add(String auctionString) { // add method PASSES TEST

		HttpHeaders headers = new HttpHeaders(); // creates header

		headers.setContentType(MediaType.APPLICATION_JSON); // defines content type (JSON)

		Auction auction = makeAuction(auctionString); // creates an auction object (using helper method)

		HttpEntity entity = makeEntity(auction); // creates an entity (using helper method)
		
		if (auction == null ) { // make sure the auction isn't null
			return null;
		}

		try { // begins try-catch block

			auction = restTemplate.postForObject(BASE_URL, entity, Auction.class); // posts new auction

			return auction; // returns new auction

		} catch (RestClientResponseException e) { // catch
			console.printError(e.getRawStatusCode() + " : " + e.getStatusText());
			return null;

		} catch (ResourceAccessException e) {
			console.printError(e.getMessage()); // catch
			return null;
		}

	}

	public Auction update(String auctionString) { // update method

		HttpHeaders headers = new HttpHeaders(); // creates header

		headers.setContentType(MediaType.APPLICATION_JSON); // defines content type (JSON)

		Auction auction = makeAuction(auctionString); // creates an auction object (using helper method)

		HttpEntity<Auction> entity = makeEntity(auction); // creates an entity (using helper method)
		
		if (auction == null ) { // make sure the auction isn't null
			return null;
		}

		try { // begins try-catch block

			restTemplate.put(BASE_URL + "auction/" + auction.getId(), entity); // edits auction

			return auction; // returns new auction

		} catch (RestClientResponseException e) { // catch
			console.printError(e.getRawStatusCode() + " : " + e.getStatusText()); // error message
			return null;
		} catch (ResourceAccessException e) { // error message
			console.printError(e.getMessage()); // catch
			return null;
		}

	}

	public boolean delete(int id) { // delete method
		
		boolean deletes = false;
		
		try { // begins try-catch block

			restTemplate.delete(BASE_URL + "/" + id);
			deletes = true;
			
		} catch (RestClientResponseException e) { // catch
			console.printError(e.getRawStatusCode() + " : " + e.getStatusText()); // error message

		} catch (ResourceAccessException e) { // catch
			console.printError(e.getMessage()); // error message
		}
		
		return deletes;
	}

	private HttpEntity<Auction> makeEntity(Auction auction) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Auction> entity = new HttpEntity<>(auction, headers);
		return entity;
	}

	private Auction makeAuction(String CSV) {
		String[] parsed = CSV.split(",");
		// invalid input
		if (parsed.length < 4 || parsed.length > 5) {
			return null;
		}
		// Add method does not include an id and only has 5 strings
		if (parsed.length == 4) {
			// Create a string version of the id and place into an array to be concatenated
			String[] withId = new String[6];
			Auction[] auctions = getAll();
			if (auctions == null) {
				return null; // Some exception or other problem occurred.
			}
			String[] idArray = new String[] { auctions.length + 1 + "" };
			// place the id into the first position of the data array
			System.arraycopy(idArray, 0, withId, 0, 1);
			System.arraycopy(parsed, 0, withId, 1, 4);
			parsed = withId;
		}
		return new Auction(Integer.parseInt(parsed[0].trim()), parsed[1].trim(), parsed[2].trim(), parsed[3].trim(),
				Double.parseDouble(parsed[4].trim()));
	}

}
