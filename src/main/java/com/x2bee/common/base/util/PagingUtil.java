package com.x2bee.common.base.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.x2bee.common.base.constant.X2Constants;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 페이징 Util
 * 
 * @author User
 *
 */
@Data
@NoArgsConstructor
@Slf4j
public class PagingUtil implements Serializable{
	private static final long serialVersionUID = -6492264354991911523L;

	private int blockCount = X2Constants.DEFAULT_BLOCK_COUNT; // 10
    private int totalCount;
    private int pageIdx = X2Constants.DEFAULT_PAGE_INDEX; // 1
    private int totalPageCount;
    private int startRowNo;
    private int endRowNo;
    private int startBlockNo;
    private int endBlockNo;
    private int rowsPerPage = X2Constants.ROWS_PER_PAGE_20;
    private int prevPageNo;
    private int nextPageNo;
    private int prevBlockNo;
    private int nextBlockNo;
    private int pageCalc;
    private List<Integer>pageList = new ArrayList<>();

    /**
     * 전체row/현재 페이지/페이지 당 row/페이징탭 사이즈로 계산
     * 
     * @param totalCount
     *            total rows count
     * @param pageIdx
     *            current page
     * @param rowsPerPage
     *            rows per page
     * @param blockCount
     *            paging tab size
     */
    public PagingUtil(int totalCount, int pageIdx, int rowsPerPage, int blockCount) {
        this.totalCount = totalCount;
        this.pageIdx = pageIdx;
        this.rowsPerPage = rowsPerPage;
        this.blockCount = blockCount;

        calculate();
    }

    /**
     * 전체row/현재 페이지/페이지 당 row로 계산 페이징탭 사이즈 = 10
     * 
     * @param totalCount
     * @param pageIdx
     * @param rowsPerPage
     */
    public PagingUtil(int totalCount, int pageIdx, int rowsPerPage) {
        this.totalCount = totalCount;
        this.pageIdx = pageIdx;
        this.rowsPerPage = rowsPerPage;

        calculate();
    }

    /**
     * 전체row/현재 페이지로 계산 페이지 당 row = 10 페이징탭 사이즈 = 10
     * 
     * @param totalCount
     * @param page
     */
    public PagingUtil(int totalCount, int pageIdx) {
        this.totalCount = totalCount;
        this.pageIdx = pageIdx;

        calculate();
    }

    /**
     * 입력된 정보로 페이징 탭 계산.
     */
    private void calculate() {
        totalPageCount = (totalCount / rowsPerPage) + (totalCount % rowsPerPage == 0 ? 0 : 1);
        endRowNo = pageIdx * rowsPerPage;
        startRowNo = (pageIdx - 1) * rowsPerPage + 1;

        startBlockNo = ((pageIdx - 1) / blockCount) * blockCount + 1;
        endBlockNo = startBlockNo + (blockCount - 1);

        // 마지막 페이지 정보
        endBlockNo = endBlockNo > totalPageCount ? totalPageCount : endBlockNo;
        
        nextPageNo = pageIdx + 1 > totalPageCount ? totalPageCount : pageIdx + 1;
        prevPageNo = pageIdx - 1  <= 1 ? 1 : pageIdx - 1;
        nextBlockNo = endBlockNo + 1 > totalPageCount ? totalPageCount : endBlockNo + 1;
        prevBlockNo = startBlockNo - blockCount  <= 1 ? 1 : startBlockNo - blockCount;
        pageCalc = rowsPerPage * (pageIdx-1);
        
        // pageList
        for (int i = startBlockNo; i <= endBlockNo; i++) {
        	pageList.add(i);
        }
    }

    /**
     * 스크롤 다운용 페이징 변수 세팅 default : 1 페이지, 20 Rows
     * 
     * @param clazz
     */
    public static final <T> void setPagingProperty(T clazz) {
        try {
            final int pageIdx = Integer.parseInt(BeanUtils.getProperty(clazz, "pageIdx"));
            final int rowsPerPage = Integer.parseInt(BeanUtils.getProperty(clazz, "rowsPerPage"));

            BeanUtils.setProperty(clazz, "pageIdx", (pageIdx < 1 ? X2Constants.DEFAULT_PAGE_INDEX : pageIdx));
            BeanUtils.setProperty(clazz, "rowsPerPage", (rowsPerPage < 1 ? X2Constants.ROWS_PER_PAGE_20 : rowsPerPage));

        } catch (Exception ignore) {
            log.trace(ignore.getMessage(), ignore);
        }
    }

    /**
     * 스크롤 다운용 페이징 변수 세팅 default : 1 페이지, 20 Rows
     * 
     * @param clazz
     * @param rowsPerPage
     */
    public static final <T> void setPagingProperty(T clazz, int rowsPerPage) {
        try {
            final int pageIdx = Integer.parseInt(BeanUtils.getProperty(clazz, "pageIdx"));

            BeanUtils.setProperty(clazz, "pageIdx", (pageIdx < 1 ? X2Constants.DEFAULT_PAGE_INDEX : pageIdx));
            BeanUtils.setProperty(clazz, "rowsPerPage", rowsPerPage);

        } catch (Exception ignore) {
            log.trace(ignore.getMessage(), ignore);
        }
    }
    
    /**
     * 숫자형식 페이징 처리를 위한 데이터.
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public static final <T> PagingUtil getPageNumbering(T clazz) {
    	
    	int totalCount = 0;
    	
    	try {
    		String totalSize = BeanUtils.getProperty(clazz, "totalSize");
    		totalSize = (totalSize == null || "".equals(totalSize.trim())) ? "0" : totalSize;
    		
    		totalCount = Integer.parseInt(totalSize);
    		totalCount = totalCount < 1 ? 0 : totalCount;
    		
    	} catch (Exception ignore) {
            log.trace(ignore.getMessage(), ignore);
        }
    	
    	return getPageNumbering(clazz, totalCount);
    }

    /**
     * 숫자형식 페이징 처리를 위한 데이터. [default:1, 페이지:20Rows]
     * 
     * @param clazz
     * @param totalCount
     * @return
     */
    public static final <T> PagingUtil getPageNumbering(T clazz, final int totalCount) {

        return getPageNumbering(clazz, totalCount, 0);
    }

    /**
     * 숫자형식 페이징 처리를 위한 데이터. [default:1, 페이지:20Rows]
     * 
     * @param clazz
     * @param totalCount
     * @param pageRows
     * @return
     */
    public static final <T> PagingUtil getPageNumbering(T clazz, final int totalCount, final int pageRows) {
        return getPageNumbering(clazz, totalCount, pageRows, 0);
    }
    
    /**
     * 숫자형식 페이징 처리를 위한 데이터. [default:1, 페이지:20Rows]
     * 
     * @param clazz
     * @param totalCount
     * @param pageRows
     * @return
     */
    public static final <T> PagingUtil getPageNumbering(T clazz, final int totalCount, final int pageRows, final int blockCount) {
        PagingUtil numberingInfo = null;
        
        try {
            
            int pageIdx = Integer.parseInt(BeanUtils.getProperty(clazz, "pageIdx"));
            int rowsPerPage = pageRows > 0 ? pageRows : Integer.parseInt(BeanUtils.getProperty(clazz, "rowsPerPage"));
            int blocks = blockCount < 1 ? X2Constants.DEFAULT_BLOCK_COUNT : blockCount; 
            
            pageIdx = pageIdx < 1 ? X2Constants.DEFAULT_PAGE_INDEX : pageIdx;
            rowsPerPage = rowsPerPage < 1 ? X2Constants.ROWS_PER_PAGE_20 : rowsPerPage;
            
            numberingInfo = new PagingUtil(totalCount, pageIdx, rowsPerPage, blocks);
            
        } catch (Exception ignore) {
            log.trace(ignore.getMessage(), ignore);
        }
        
        return numberingInfo;
    }

}
