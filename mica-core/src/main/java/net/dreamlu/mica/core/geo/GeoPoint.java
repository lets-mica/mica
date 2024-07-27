package net.dreamlu.mica.core.geo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 坐标点
 *
 * @author JourWon、hutool、L.cm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPoint implements Serializable {
	private static final long serialVersionUID = 3584864663880053897L;

	/**
	 * 经度
	 */
	private double lon;
	/**
	 * 纬度
	 */
	private double lat;

	/**
	 * 当前坐标偏移指定坐标
	 *
	 * @param offset 偏移量
	 * @return this
	 */
	public GeoPoint offset(GeoPoint offset) {
		this.lon += offset.lon;
		this.lat += offset.lat;
		return this;
	}

}
