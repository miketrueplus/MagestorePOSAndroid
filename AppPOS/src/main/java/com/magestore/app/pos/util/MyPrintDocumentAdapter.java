package com.magestore.app.pos.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;

import java.io.FileOutputStream;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MyPrintDocumentAdapter extends PrintDocumentAdapter{

	private Context mContext;
	private String mPrintString;
	private PdfDocument mPdfDocument;
	private int mTotalPages;
	private int mPageWidth;
	private int mPageHeight;

	public MyPrintDocumentAdapter(Context context, String printString) {

		mContext = context;
		mPrintString = printString;
		mTotalPages = 1;
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

		// Create a new PdfDocument with the requested page attributes
		mPdfDocument = new PrintedPdfDocument(mContext, newAttributes);

		mPageWidth = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;
		mPageHeight = newAttributes.getMediaSize().getHeightMils()/1000 * 72;

		// Respond to cancellation request
		if (cancellationSignal.isCanceled()) {
			callback.onLayoutCancelled();
			return;
		}

		// Compute the expected number of printed pages
//		mTotalPages = computePageCount(newAttributes);

		if (mTotalPages > 0) {
			PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("print_output.pdf");
			builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT);
			builder.setPageCount(mTotalPages);
			PrintDocumentInfo info = builder.build();

			// Content layout reflow is complete
			callback.onLayoutFinished(info, true);
		} else {
			// Otherwise report an error to the print_preview framework
			callback.onLayoutFailed("Page count calculation failed.");
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

		// Iterate over each page of the document,
		// check if it's in the output range.
		for (int i = 0; i < mTotalPages; i++) {
			// Check to see if this page is in the output range.
			if (pageInRange(pageRanges, i)) {

				PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(mPageWidth, mPageHeight, i).create();
				PdfDocument.Page page = mPdfDocument.startPage(newPage);

				// check for cancellation
				if (cancellationSignal.isCanceled()) {
					callback.onWriteCancelled();
					mPdfDocument.close();
					mPdfDocument = null;
					return;
				}

				// Draw page content for printing
				drawPage(page, i);

				// Rendering is complete, so page can be finalized.
				mPdfDocument.finishPage(page);
			}
		}

		try {
			mPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
		} catch (IOException e) {
			callback.onWriteFailed(e.toString());
			return;
		} finally {
			mPdfDocument.close();
			mPdfDocument = null;
		}

		callback.onWriteFinished(pageRanges);
	}

//	private int computePageCount(PrintAttributes printAttributes) {
//		int itemsPerPage = 4; // default item count for portrait mode
//
//		PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
//		if (!pageSize.isPortrait()) {
//			// Six items per page in landscape orientation
//			itemsPerPage = 6;
//		}
//
//		// Determine number of print_preview items
//		int printItemCount = getPrintItemCount();
//
//		return (int) Math.ceil(printItemCount / itemsPerPage);
//	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private boolean pageInRange(PageRange[] pageRanges, int page)
	{
		for (int i = 0; i<pageRanges.length; i++)
		{
			if ((page >= pageRanges[i].getStart()) &&
					(page <= pageRanges[i].getEnd()))
				return true;
		}
		return false;
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private void drawPage(PdfDocument.Page page, int pagenumber) {
		Canvas canvas = page.getCanvas();

		pagenumber++; // Make sure page numbers start at 1

		int titleBaseLine = 72;
		int leftMargin = 54;

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(40);
		canvas.drawText("Test Print Document Page " + pagenumber, leftMargin, titleBaseLine, paint);

		paint.setTextSize(14);
		canvas.drawText(mPrintString, leftMargin, titleBaseLine + 35, paint);

		if (pagenumber % 2 == 0)
			paint.setColor(Color.RED);
		else
			paint.setColor(Color.GREEN);

		PdfDocument.PageInfo pageInfo = page.getInfo();
	}
}
