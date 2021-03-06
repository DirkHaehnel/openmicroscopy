#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
   gateway tests - Testing the gateway image wrapper.getPrimaryPixels() and the pixels wrapper

"""

import unittest
import omero
import time

import gatewaytest.library as lib


class PixelsTest (lib.GTest):

    def setUp (self):
        super(PixelsTest, self).setUp()
        self.loginAsAuthor()
        self.TESTIMG = self.getTestImage()

    def testReuseRawPixelsStore (self):
        img1 = self.TESTIMG
        img2 = self.getTestImage2()
        rps = self.gateway.createRawPixelsStore()
        rps.setPixelsId(img1.getPrimaryPixels().getId(), True, {'omero.group': '-1'})
        self.assert_(rps.getByteWidth() > 0)
        rps.setPixelsId(img2.getPrimaryPixels().getId(), True, {'omero.group': '-1'})
        self.assert_(rps.getByteWidth() > 0)

    def testPlaneInfo(self):

        image = self.TESTIMG
        pixels = image.getPrimaryPixels()
        self.assertEqual(pixels.OMERO_CLASS, 'Pixels')
        self.assertEqual(pixels._obj.__class__, omero.model.PixelsI)
        sizeZ = image.getSizeZ()
        sizeC = image.getSizeC()
        sizeT = image.getSizeT()
        planeInfo = list(pixels.copyPlaneInfo())
        self.assertEqual(len(planeInfo), sizeZ*sizeC*sizeT)

        # filter by 1 or more dimension
        planeInfo = list(pixels.copyPlaneInfo(theC=0))
        for p in planeInfo:
            self.assertEqual(p.theC, 0)
        planeInfo = list(pixels.copyPlaneInfo(theZ=1, theT=0))
        for p in planeInfo:
            self.assertEqual(p.theZ, 1)
            self.assertEqual(p.theT, 0)

    def testPixelsType(self):
        image = self.TESTIMG
        pixels = image.getPrimaryPixels()

        pixelsType = pixels.getPixelsType()
        self.assertEqual(pixelsType.value, 'int16')
        self.assertEqual(pixelsType.bitSize, 16)

    def testGetTile(self):
        image = self.TESTIMG
        pixels = image.getPrimaryPixels()

        sizeZ = image.getSizeZ()
        sizeC = image.getSizeC()
        sizeT = image.getSizeT()
        sizeX = image.getSizeX()
        sizeY = image.getSizeY()

        zctTileList = []
        tile = (50,100,10,20)
        for z in range(2):
            for c in range(1):
                for t in range(1):
                    zctTileList.append((z,c,t, tile))

        lastTile = None
        tiles = pixels.getTiles(zctTileList)  # get a tile from every plane
        for tile in tiles:
            lastTiles = tile

        lastT = None
        for zctTile in zctTileList:
            z,c,t, Tile = zctTile
            tile = pixels.getTile(z,c,t, Tile)
        self.assertEqual(lastTile, lastT)

        # try stacking tiles together - check it's the same as getting the same region as 1 tile
        z, c, t = 0, 0, 0
        tile1 = pixels.getTile(z,c,t, (0, 0, 5, 3))
        tile2 = pixels.getTile(z,c,t, (5, 0, 5, 3))
        tile3 = pixels.getTile(z,c,t, (0, 0, 10, 3))  # should be same as tile1 and tile2 combined
        from numpy import hstack
        stacked = hstack((tile1, tile2))
        self.assertEqual(str(tile3), str(stacked))  # bit of a hacked way to compare arrays, but seems to work

    def testGetPlane(self):
        image = self.TESTIMG
        pixels = image.getPrimaryPixels()

        sizeZ = image.getSizeZ()
        sizeC = image.getSizeC()
        sizeT = image.getSizeT()

        zctList = []
        for z in range(sizeZ):
            for c in range(sizeC):
                for t in range(sizeT):
                    zctList.append((z,c,t))

        # timing commented out below - typical times:
        # get 70 planes, using getPlanes() t1 = 3.99837493896 secs, getPlane() t2 = 5.9151828289 secs. t1/t2 = 0.7
        # get 210 planes, using getPlanes() t1 = 12.3150248528 secs, getPlane() t2 = 17.2735779285 secs t1/t2 = 0.7

        # test getPlanes()
        #import time
        #startTime = time.time()
        planes = pixels.getPlanes(zctList)  # get all planes
        for plane in planes:
            p = plane
        #t1 = time.time() - startTime
        #print "Getplanes = %s secs" % t1

        # test getPlane() which returns a single plane
        #startTime = time.time()
        for zct in zctList:
            z,c,t = zct
            p = pixels.getPlane(z,c,t)
        #t2 = time.time() - startTime
        #print "Get individual planes = %s secs" % t2
        #print "t1/t2", t1/t2

        lastPlane = pixels.getPlane(sizeZ-1, sizeC-1, sizeT-1)
        plane = pixels.getPlane()   # default is (0,0,0)
        firstPlane = pixels.getPlane(0,0,0)
        self.assertEqual(plane[0][0], firstPlane[0][0])

    def testGetPlanesExceptionOnGetPlane(self):
        """
        Tests exception handling in the gateway.getPlanes generator.

        See #5156
        """
        image = self.TESTIMG
        pixels = image.getPrimaryPixels()

        # Replace service creation with a mock
        pixels._prepareRawPixelsStore = lambda: MockRawPixelsStore(pixels)

        # Now, when we call, the first yield should succeed, the second should fail
        found = 0
        try:
            for x in pixels.getPlanes(((0,0,0), (1,1,1))):
                found += 1
            self.fail("Should throw")
        except AssertionError:
            raise
        except Exception, e:
            self.assert_(not e.close)
            self.assertEquals(1, found)

    def testGetPlanesExceptionOnClose(self):
        """
        Tests exception handling in the gateway.getPlanes generator.

        See #5156
        """
        image = self.TESTIMG
        pixels = image.getPrimaryPixels()

        # Replace service creation with a mock
        pixels._prepareRawPixelsStore = lambda: MockRawPixelsStore(pixels, good_calls = 2, close_fails = True)

        # Now, when we call, the first yield should succeed, the second should fail
        found = 0
        try:
            for x in pixels.getPlanes(((0,0,0), (1,1,1))):
                found += 1
            self.fail("Should have failed on close")
        except AssertionError:
            raise
        except Exception, e:
            self.assert_(e.close)
            self.assertEquals(2, found)

    def testGetPlanesExceptionOnBoth(self):
        """
        Tests exception handling in the gateway.getPlanes generator.

        In this test, both the getPlane and the close throw an exception.
        The exception from the getPlane method should be thrown, and the close
        logged (not tested here)

        See #5156
        """
        image = self.TESTIMG
        pixels = image.getPrimaryPixels()

        # Replace service creation with a mock
        pixels._prepareRawPixelsStore = lambda: MockRawPixelsStore(pixels, good_calls = 1, close_fails = True)

        # Now, when we call, the first yield should succeed, the second should fail
        found = 0
        try:
            for x in pixels.getPlanes(((0,0,0), (1,1,1))):
                found += 1
            self.fail("Should have failed on getPlane and close")
        except AssertionError:
            raise
        except Exception, e:
            self.assert_(not e.close)
            self.assertEquals(1, found)


class MockRawPixelsStore(object):

    """
    Mock which throws exceptions at given times.
    """

    def __init__(self, pixels, good_calls = 1, close_fails = False):
        self.pixels = pixels
        self.good_calls = good_calls
        self.close_fails = close_fails

    def getPlane(self, *args):
        if self.good_calls == 0:
            e = Exception("MOCK EXCEPTION")
            e.close = False
            raise e
        else:
            self.good_calls -= 1
            return "0"*(2*self.pixels.getSizeX()*self.pixels.getSizeY())

    def close(self, *args):
        if self.close_fails:
            e = Exception("MOCK CLOSE EXCEPTION")
            e.close = True
            raise e


if __name__ == '__main__':
    unittest.main()
