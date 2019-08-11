package com.tianlun.ad.search;

import com.tianlun.ad.search.vo.SearchRequest;
import com.tianlun.ad.search.vo.SearchResponse;

public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);
}
