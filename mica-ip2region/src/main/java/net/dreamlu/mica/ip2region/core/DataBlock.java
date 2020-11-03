package net.dreamlu.mica.ip2region.core;

/**
 * data block class
 *
 * @author chenxin<chenxin619315 @ gmail.com>
 */
public class DataBlock {

	/**
	 * city id
	 */
	private int cityId;

	/**
	 * region address
	 */
	private String region;

	/**
	 * region ptr in the db file
	 */
	private int dataPtr;

	/**
	 * construct method
	 *
	 * @param cityId city id
	 * @param region  region string
	 * @param dataPtr data ptr
	 */
	public DataBlock(int cityId, String region, int dataPtr) {
		this.cityId = cityId;
		this.region = region;
		this.dataPtr = dataPtr;
	}

	public DataBlock(int cityId, String region) {
		this(cityId, region, 0);
	}

	public int getCityId() {
		return cityId;
	}

	public DataBlock setCityId(int cityId) {
		this.cityId = cityId;
		return this;
	}

	public String getRegion() {
		return region;
	}

	public DataBlock setRegion(String region) {
		this.region = region;
		return this;
	}

	public int getDataPtr() {
		return dataPtr;
	}

	public DataBlock setDataPtr(int dataPtr) {
		this.dataPtr = dataPtr;
		return this;
	}

	@Override
	public String toString() {
		return String.valueOf(cityId) + '|' + region + '|' + dataPtr;
	}

}
