package com.SV.timezone;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;

/**
 * 
 * @author Saurabh Vaidya
 *
 */
public class Application {
	private static final String API_KEY = "AIzaSyApVcklJpPOvIP5pa8IaUcWG6AtEaSnMNI";
	private static GeoApiContext context = new GeoApiContext.Builder().apiKey(
			API_KEY).build();

	/**
	 * Start of application
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Application app = new Application();
		app.calculate();
	}

	/**
	 * Calculation of read and excute UTC timezone
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void calculate() throws IOException, URISyntaxException {
		List<String> inputData = readCsvFile();
		List<String> outputData = findTimeZone(inputData);
		for (String o : outputData)
			System.out.println(o);
	}

	/**
	 * Read from CSV file but due to issue reading from txt file
	 * 
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public List<String> readCsvFile() throws IOException, URISyntaxException {
		Path filePath = Paths.get(new Application().getClass().getClassLoader()
				.getResource("input.txt").toURI());
		List<String> input = Files.readAllLines(filePath);
		return input;
	}

	/**
	 * This is the actual function to convert to timezone
	 * 
	 * @param inputData
	 * @return
	 */
	List<String> findTimeZone(List<String> inputData) {
		List<String> outputList = new ArrayList();
		String output = null;
		for (String input : inputData) {
			String[] split = input.split(",");
			String dateTime = split[0];
			Double latitude = Double.valueOf(split[1]);
			Double longitude = Double.valueOf(split[2]);
			LatLng coords = new LatLng(latitude, longitude);
			TimeZone timeZone;
			try {
				timeZone = TimeZoneApi.getTimeZone(context, coords).await();
				LocalDateTime localDateTime = convertToUtc(dateTime,
						timeZone.getID());
				output = input.concat(",").concat(timeZone.getID()).concat(",")
						.concat(localDateTime.toString());
			} catch (IOException e) {
				System.out.println("IOException" + e.getMessage());
			} catch (ApiException e) {
				System.out.println(e);
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException::" + e.getMessage());
			} catch (DateTimeParseException e) {
				System.out.println("Date not in correct format::"
						+ e.getMessage());
			}
			if (output != null)
				outputList.add(output);
		}
		return outputList;
	}

	/**
	 * Actual function to get into the respective format as per timezone
	 * 
	 * @param dateTime
	 * @param timeZone
	 * @return
	 */
	public LocalDateTime convertToUtc(String dateTime, String timeZone) {
		final DateTimeFormatter UTCTimeFormat = DateTimeFormatter.ofPattern(
				"yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
		final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime,
				UTCTimeFormat);
		return LocalDateTime.ofInstant(zonedDateTime.toInstant(),
				ZoneId.of(timeZone));
	}
}
